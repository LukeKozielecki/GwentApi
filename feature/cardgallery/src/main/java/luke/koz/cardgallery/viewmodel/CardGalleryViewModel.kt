package luke.koz.cardgallery.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import luke.koz.domain.NetworkConnectivityChecker
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.domain.repository.AuthStatusRepository
import luke.koz.domain.repository.CardGalleryRepository
import luke.koz.domain.repository.UserLikesDataSource
import luke.koz.presentation.CardState
import java.io.IOException

class CardGalleryViewModel (
    private val repository: CardGalleryRepository,
    private val userLikesDataSource: UserLikesDataSource,
    private val authStatusRepository: AuthStatusRepository,
    private val networkConnectivityChecker: NetworkConnectivityChecker

) : ViewModel (){
    private val _cardState = mutableStateOf<CardState>(CardState.Empty)
    val cardState: State<CardState> = _cardState

    private val _rawCardsFlow = MutableStateFlow<List<CardGalleryEntry>>(emptyList())
    private val _likedCardIdsFlow = MutableStateFlow<Set<Int>>(emptySet())
    private val _allCardLikesFlow = MutableStateFlow<Map<Int, Set<String>>>(emptyMap())
    private val _isInitialLoadingComplete = MutableStateFlow(false)

    private val _currentUserId = MutableStateFlow<String?>(null)
    private val _hasInitialAuthResolved = MutableStateFlow(false)
    private val _wasInternetInitiallyUnavailable = MutableStateFlow(false)

    init {
        _cardState.value = CardState.Loading
        setupDebuggingObservers()
        configureCardStateUpdates()
        observeAuthAndInternetChanges()
        getAllCards()
    }

    private fun setupDebuggingObservers() {
        _rawCardsFlow
            .onEach { Log.d("FlowDebug", "Raw cards updated: ${it.size} entries") }
            .launchIn(viewModelScope)

        _likedCardIdsFlow
            .onEach { Log.d("FlowDebug", "Liked IDs updated: $it") }
            .launchIn(viewModelScope)

        _allCardLikesFlow
            .onEach { Log.d("FlowDebug", "All likes updated: $it") }
            .launchIn(viewModelScope)
    }

    private fun configureCardStateUpdates() {
        combine(
            _rawCardsFlow,
            _likedCardIdsFlow,
            _allCardLikesFlow,
            _isInitialLoadingComplete
        ) { rawCards, likedIds, allLikes, isInitialLoadingComplete ->
            val cardsWithLikes = mapCardsToGalleryEntries(rawCards, likedIds, allLikes)
            Pair(cardsWithLikes, isInitialLoadingComplete)
        }
            .flowOn(Dispatchers.Default)
            .onEach { (combinedCards, isInitialLoadingComplete) ->
                _cardState.value = when {
                    !isInitialLoadingComplete -> CardState.Loading
                    combinedCards.isEmpty() -> CardState.Empty
                    else -> CardState.Success(combinedCards)
                }
            }
            .catch { e ->
                _cardState.value = CardState.Error("Error: ${e.message}")
            }
            .launchIn(viewModelScope)
    }

    private fun mapCardsToGalleryEntries(
        rawCards: List<CardGalleryEntry>,
        likedIds: Set<Int>,
        allLikes: Map<Int, Set<String>>
    ): List<CardGalleryEntry> {
        return rawCards.map { card ->
            card.copy(
                isLiked = likedIds.contains(card.id),
                likeCount = allLikes[card.id]?.size ?: 0
            )
        }
    }


    private fun observeAuthAndInternetChanges() {
        combine(
            authStatusRepository.observeCurrentUser(),
            networkConnectivityChecker.observeInternetAvailability()
        ) { authUserModel, isInternetAvailable ->
            val previousUserId = _currentUserId.value
            _currentUserId.value = authUserModel?.id
            Log.d("CardGalleryVM", "Combined observer: User: ${authUserModel?.email ?: "null"}, Internet: $isInternetAvailable")

            // Prevent triggering refresh if initial load isn't yet complete
            if (!_isInitialLoadingComplete.value) {
                Log.d("CardGalleryVM", "Combined observer: Initial loading not complete, skipping refresh logic.")
                return@combine // Break
            }

            // Handle internet re-connection after initial offline state
            if (_wasInternetInitiallyUnavailable.value && isInternetAvailable && _isInitialLoadingComplete.value) {
                _wasInternetInitiallyUnavailable.value = false // Reset the flag once handled
                Log.d("CardGalleryVM", "Internet reconnected after initial offline state, triggering refresh.")
                refreshLikesForCurrentUser(_currentUserId.value)
                return@combine // Break
            }

            // Only consider refreshing if initial auth state has been resolved and initial loading is complete
            if (_hasInitialAuthResolved.value && _isInitialLoadingComplete.value) {
                val currentUserId = authUserModel?.id

                val shouldRefresh = when {
                    //todo this COULD be stored locally and in case no internet load last relevatn
                    //  snapshot from memory. this doesn't seem too `too` important
                    previousUserId != null && currentUserId == null -> { // Case for user logging out **without internet** (because of course)
                        fetchLikedCardsByUser(null)// Only refresh User-specific ui, preserve previous snapshot of likes numbers
                        false // Don't refresh likes totals
                    }
                    !isInternetAvailable -> false // No internet, don't refresh
                    currentUserId != previousUserId -> true // User changed (login/logout)
                    isInternetAvailable && previousUserId != null -> { // Internet available AND user is logged in
                        // This handles cases where internet drops and comes back for an already logged-in user
                        true
                    }
                    else -> false
                }

                if (shouldRefresh) {
                    Log.d("CardGalleryVM", "Triggering refreshLikesForCurrentUser due to combined observer change.")
                    refreshLikesForCurrentUser(currentUserId)
                }
            }
        }.launchIn(viewModelScope)
    }

//    private fun observeAuthenticationStatus() {
//        authStatusRepository.observeCurrentUser()
//            .onEach { authUserModel ->
//                _currentUserId.value = authUserModel?.id
//                Log.d("CardGalleryVM", "Auth status changed via repo. User: ${authUserModel?.email ?: "null"}")
//
//                refreshLikesForCurrentUser(authUserModel?.id)
//            }
//            .launchIn(viewModelScope)
//    }
//
//    private fun observeInternetAndRefreshLikes() {
//        networkConnectivityChecker.observeInternetAvailability()
//            .filter { isAvailable -> isAvailable }
//            .onEach {
//                _currentUserId.value?.let { userId ->
//                    refreshLikesForCurrentUser(userId)
//                }
//            }
//            .launchIn(viewModelScope)
//    }

    fun getAllCards(forceRefresh: Boolean = false) {
        Log.d("CardGalleryVM", "viewModelScope.launch: getAllCards")
        viewModelScope.launch {
            _cardState.value = CardState.Loading
            _isInitialLoadingComplete.value = false
            if (!forceRefresh) {
                _cardState.value = CardState.Loading
                _isInitialLoadingComplete.value = false
            }

            try {
                // Determine if internet is available at the start of getAllCards
                val initialInternetStatus = networkConnectivityChecker.observeInternetAvailability().first()
                if (!initialInternetStatus) {
                    _wasInternetInitiallyUnavailable.value = true
                    Log.d("CardGalleryVM", "Initial internet status: OFFLINE. Setting _wasInternetInitiallyUnavailable to true.")
                } else {
                    _wasInternetInitiallyUnavailable.value = false
                }


                val initialAuthUser = authStatusRepository.observeCurrentUser().first()
                _currentUserId.value = initialAuthUser?.id
                _hasInitialAuthResolved.value = true
                Log.d("CardGalleryVM", "Initial auth user resolved in getAllCards: ${initialAuthUser?.email ?: "null"}")
                val initialCardsLoad = async {
                    repository.getAllCards(forceRefresh)
                        .onEach { cards ->
                            _rawCardsFlow.value = cards
                            Log.d("CardGalleryVM", "Initial cards emitted and updated: ${cards.size}")
                        }
                        .first()
                    Log.d("CardGalleryVM", "Initial cards collection completed for awaitAll.")
                }

                val likesDeferred = async {
                    refreshLikesForCurrentUser(_currentUserId.value)
                }

                awaitAll(initialCardsLoad, likesDeferred)

                _isInitialLoadingComplete.value = true
                Log.d("CardGalleryVM", "_isInitialLoadingComplete set to true.")

            } catch (e: Exception) {
                _cardState.value = CardState.Error(e.message ?: "Failed to load cards")
                _isInitialLoadingComplete.value = true
                Log.e("CardGalleryVM", "Error in getAllCards: ${e.message}", e)
            }
        }
    }

    private suspend fun refreshLikesForCurrentUser(currentUserId: String? = null) {
        Log.d("CardGalleryVM", "refreshLikesForCurrentUser called.")
        try {
            Log.d("CardGalleryVM","Current user ID for likes refresh: ${currentUserId ?: "null"}")
            fetchLikedCardsByUser(currentUserId)
            fetchAllCardLikes()
        } catch (e: Exception) {
            Log.e("CardGalleryVM", "Error refreshing likes: ${e.message}", e)
            throw e
        }
    }

    private suspend fun fetchLikedCardsByUser(currentUserId: String?) {
        // fetch liked cards by user
        _likedCardIdsFlow.value = currentUserId?.let {
            userLikesDataSource.getLikedCardIdsForUser(it).also { likedIds ->
                Log.d("CardGalleryVM", "Liked IDs fetched: ${likedIds.size}")
            }
        } ?: emptySet()
    }

    private suspend fun fetchAllCardLikes() {
        // fetch total likes
        userLikesDataSource.getLikesForAllCards().also { allLikes ->
            Log.d("CardGalleryVM", "All likes fetched: ${allLikes.size}")
            _allCardLikesFlow.value = allLikes
        }
    }

    fun toggleLike(cardId: Int, isCurrentlyLiked: Boolean) {
        val userId = _currentUserId.value ?: run {
            _cardState.value = CardState.Error("Authentication required")
            return
        }

        viewModelScope.launch {
            //todo: removed optimistic update, perhaps should have VM for managing UI being enabled
            //  observing InternetConnection
            repository.toggleCardLike(userId, cardId, !isCurrentlyLiked)
                .onSuccess {
                    Log.d("CardGalleryVM", "Like toggle successful for card $cardId.")
                    _likedCardIdsFlow.update { ids ->
                        if (isCurrentlyLiked) ids - cardId else ids + cardId
                    }
                    _allCardLikesFlow.update { likesMap ->
                        likesMap.toMutableMap().apply {
                            val users = getOrPut(cardId) { mutableSetOf() }.toMutableSet()
                            if (isCurrentlyLiked) users.remove(userId) else users.add(userId)
                            if (users.isEmpty()) remove(cardId) else put(cardId, users)
                        }
                    }
                }
                .onFailure { e ->
                    //todo communicate error to user with toast
                    val errorMessage = when (e) {
                        is IOException -> "No internet connection. Please check your network."
                        else -> "Failed to update like: ${e.message ?: "Unknown error"}"
                    }
                    Log.e("CardGalleryVM", "Like update for card $cardId failed: $errorMessage", e)
                }
        }
    }
}