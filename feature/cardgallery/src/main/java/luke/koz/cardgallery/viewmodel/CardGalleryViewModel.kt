package luke.koz.cardgallery.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import luke.koz.domain.NetworkConnectivityChecker
import luke.koz.domain.cardgallery.GetCardGalleryDataUseCase
import luke.koz.domain.cardgallery.RefreshCardGalleryDataUseCase
import luke.koz.domain.cardgallery.ToggleCardLikeUseCase
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.domain.repository.AuthStatusRepository
import luke.koz.presentation.state.CardGalleryState
import java.io.IOException

class CardGalleryViewModel (
    private val getCardGalleryDataUseCase: GetCardGalleryDataUseCase,
    private val refreshCardGalleryDataUseCase: RefreshCardGalleryDataUseCase,
    private val toggleCardLikeUseCase: ToggleCardLikeUseCase,
    private val authStatusRepository: AuthStatusRepository,
    private val networkConnectivityChecker: NetworkConnectivityChecker
) : ViewModel (){
    private val _cardGalleryState = mutableStateOf<CardGalleryState>(CardGalleryState.Loading)
    val cardGalleryState: State<CardGalleryState> = _cardGalleryState

    private val _rawCardsFlow = MutableStateFlow<List<CardGalleryEntry>>(emptyList())
    private val _likedCardIdsFlow = MutableStateFlow<Set<Int>>(emptySet())
    private val _allCardLikesFlow = MutableStateFlow<Map<Int, Set<String>>>(emptyMap())
    private val _isInitialLoadingComplete = MutableStateFlow(false)

    private val _currentUserId = MutableStateFlow<String?>(null)
    private val _hasInitialAuthResolved = MutableStateFlow(false)
    private val _wasInternetInitiallyUnavailable = MutableStateFlow(false)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing : StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
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
        createCardGalleryDataFlow()
            .flowOn(Dispatchers.Default)
            .onEach { (combinedCards, isInitialLoadingComplete) ->
                _cardGalleryState.value = getCardGalleryState(combinedCards, isInitialLoadingComplete)
            }
            .catch { e ->
                _cardGalleryState.value = CardGalleryState.Error("Error: ${e.message}")
            }
            .launchIn(viewModelScope)
    }

    private fun createCardGalleryDataFlow(): Flow<Pair<List<CardGalleryEntry>, Boolean>> {
        return combine(
            _rawCardsFlow,
            _likedCardIdsFlow,
            _allCardLikesFlow,
            _isInitialLoadingComplete
        ) { rawCards, likedIds, allLikes, isInitialLoadingComplete ->
            val cardsWithLikes = applyLikeDataToCards(rawCards, likedIds, allLikes)
            Pair(cardsWithLikes, isInitialLoadingComplete)
        }
    }

    private fun getCardGalleryState(
        combinedCards: List<CardGalleryEntry>,
        isInitialLoadingComplete: Boolean
    ): CardGalleryState {
        return when {
            !isInitialLoadingComplete -> CardGalleryState.Loading
            combinedCards.isEmpty() -> CardGalleryState.Empty
            else -> CardGalleryState.Success(combinedCards)
        }
    }

    private fun applyLikeDataToCards(
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
        refreshCardGalleryDataUseCase.observeAuthAndInternetChanges()
            .onEach { (currentUserId, isInternetAvailable) ->
                val previousUserId = _currentUserId.value
                _currentUserId.value = currentUserId
                Log.d("CardGalleryVM", "Combined observer: User: ${currentUserId ?: "null"}, Internet: $isInternetAvailable")

                // Prevent triggering refresh if initial load isn't yet complete
                if (!_isInitialLoadingComplete.value) {
                    Log.d("CardGalleryVM", "Combined observer: Initial loading not complete, skipping refresh logic.")
                    return@onEach
                }

                // Handle internet re-connection after initial offline state
                handleInternetReconnection(
                    isInternetAvailable = isInternetAvailable
                )

                // Only consider refreshing if initial auth state has been resolved and initial loading is complete
                handleAuthAndInternetBasedRefresh(
                    previousUserId = previousUserId,
                    currentUserId = currentUserId,
                    isInternetAvailable = isInternetAvailable
                )

            }.launchIn(viewModelScope)
    }

    private fun handleInternetReconnection(isInternetAvailable: Boolean) {
        if (_wasInternetInitiallyUnavailable.value && isInternetAvailable) {
            /*In case of initial boot up of the app without internet, user gets CardState.Empty
            * Then if they reconnect to the Internet, this block prevents abrupt CardState switch from
            * CardState.Empty -> CardState.Success with middle step of CardState.Loading
            */
            if (_cardGalleryState.value == CardGalleryState.Empty) { _cardGalleryState.value = CardGalleryState.Loading }

            _wasInternetInitiallyUnavailable.value = false
            Log.d("CardGalleryVM", "Internet reconnected after initial offline state, triggering refresh.")
            triggerDataRefresh()
        }
    }

    private fun handleAuthAndInternetBasedRefresh(
        previousUserId: String?,
        currentUserId: String?,
        isInternetAvailable: Boolean
    ) {
        // Only consider refreshing if initial auth state has been resolved and initial loading is complete
        if (_hasInitialAuthResolved.value) {
            val shouldRefresh = when {
                //todo this COULD be stored locally and in case no internet load last relevant
                //  snapshot from memory. this doesn't seem too `too` important
                previousUserId != null && currentUserId == null -> { // Case for user logging out **without internet** (because of course)
                    _likedCardIdsFlow.value = emptySet()// Only refresh User-specific ui, preserve previous snapshot of likes
                    Log.d("CardGalleryVM", "User logged out. Cleared liked IDs.")
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
                Log.d("CardGalleryVM", "Triggering full data refresh due to combined observer change.")
                triggerDataRefresh()
            }
        }
    }

    private fun triggerDataRefresh() {
        viewModelScope.launch {
            try {
                // Destructure the custom data class
                fetchAndProcessCardData(true)
                Log.d("CardGalleryVM", "Data refreshed successfully.")
            } catch (e: Exception) {
                Log.e("CardGalleryVM", "Error during data refresh: ${e.message}", e)
            }
        }
    }

    fun getAllCards(forceRefresh: Boolean = false) {
        Log.d("CardGalleryVM", "viewModelScope.launch: getAllCards")
        viewModelScope.launch {
            initializeCardLoadingState()
            try {
                performInitialConnectionAndAuthChecks()

                fetchAndProcessCardData(forceRefresh)

                finalizeCardLoading()
            } catch (e: Exception) {
                _cardGalleryState.value = CardGalleryState.Error(e.message ?: "Failed to load cards")
                _isInitialLoadingComplete.value = true
                Log.e("CardGalleryVM", "Error in getAllCards: ${e.message}", e)
            }
        }
    }

    private fun initializeCardLoadingState() {
        _cardGalleryState.value = CardGalleryState.Loading
        _isInitialLoadingComplete.value = false
    }

    private suspend fun performInitialConnectionAndAuthChecks() {
        val initialInternetStatus = networkConnectivityChecker.observeInternetAvailability().first()
        _wasInternetInitiallyUnavailable.value = !initialInternetStatus
        Log.d("CardGalleryVM", "Initial internet status: ${if (initialInternetStatus) "ONLINE" else "OFFLINE"}.")
        Log.d("CardGalleyVM", "Setting _wasInternetInitiallyUnavailable to ${!initialInternetStatus}.")

        val initialAuthUser = authStatusRepository.observeCurrentUser().first()
        _currentUserId.value = initialAuthUser?.id
        _hasInitialAuthResolved.value = true
        Log.d("CardGalleryVM", "Initial auth user resolved in getAllCards: ${initialAuthUser?.email ?: "null"}")
    }

    private fun finalizeCardLoading() {
        _isInitialLoadingComplete.value = true
        Log.d("CardGalleryVM", "_isInitialLoadingComplete set to true.")
    }

    fun toggleLike(cardId: Int, isCurrentlyLiked: Boolean) {
        val userId = _currentUserId.value ?: run {
            _cardGalleryState.value = CardGalleryState.Error("Authentication required")
            return
        }

        viewModelScope.launch {
            toggleCardLikeUseCase.invoke(userId, cardId, isCurrentlyLiked)
                .onSuccess {
                    handleToggleLikeSuccess(
                        userId = userId,
                        cardId = cardId,
                        isCurrentlyLiked = isCurrentlyLiked
                    )
                }
                .onFailure { e ->
                    handleToggleLikeFailure(
                        cardId = cardId,
                        e = e
                    )
                }
        }
    }

    private fun handleToggleLikeSuccess(userId: String, cardId: Int, isCurrentlyLiked: Boolean) {
        Log.d("CardGalleryVM", "Like toggle successful for card $cardId.")
        // Update _likedCardIdsFlow
        _likedCardIdsFlow.update { ids ->
            if (isCurrentlyLiked) ids - cardId else ids + cardId
        }
        // Update _allCardLikesFlow
        _allCardLikesFlow.update { likesMap ->
            likesMap.toMutableMap().apply {
                val users = getOrPut(cardId) { mutableSetOf() }.toMutableSet()
                if (isCurrentlyLiked) users.remove(userId) else users.add(userId)
                if (users.isEmpty()) remove(cardId) else put(cardId, users)
            }
        }
    }

    private fun handleToggleLikeFailure(cardId: Int, e: Throwable) {
        //todo communicate error to user with toast
        val errorMessage = when (e) {
            is IOException -> "No internet connection. Please check your network."
            else -> "Failed to update like: ${e.message ?: "Unknown error"}"
        }
        Log.e("CardGalleryVM", "Like update for card $cardId failed: $errorMessage", e)
    }

    // todo this COULD ask user with an Alert Dialog what user wishes to refresh - likes cards or all
    /**
     * This function is intended to be called on `Pull to refresh` user action collected.
     * Function stores the [CardGalleryState] it originated from, in case it cant fetch data it
     * reverts to the previous state. Than it attempts to call [refreshCardGalleryDataUseCase]
     * to fetch the most up to date data.
     * On exception it logs the error and reverts [cardGalleryState] to the previous state.
     */
    fun onPullToRefresh() {
        val initialCardState = _cardGalleryState.value
        /* This _isRefreshing is required because otherwise the indicator won't clear*/
        _isRefreshing.value = true
        _cardGalleryState.value = CardGalleryState.Loading
        viewModelScope.launch {
            try {
                /* This delay and _isRefreshing is here because otherwise the indicator won't clear */
                delay(10)
                _isRefreshing.value = false

                // Force refresh cards
                fetchAndProcessCardData(true)
            } catch (e: Exception) {
                Log.e("CardGalleryVM", "Refresh failed: ${e.message}", e)
            } finally {
                //todo add Toast to user to communicate what has happened
                _cardGalleryState.value = initialCardState
            }
        }
    }

    private suspend fun fetchAndProcessCardData(forceRefresh: Boolean) {
        val cardGalleryData = refreshCardGalleryDataUseCase.invoke(forceRefreshCards = forceRefresh)
        _rawCardsFlow.value = cardGalleryData.rawCards
        _likedCardIdsFlow.value = cardGalleryData.likedCardIds
        _allCardLikesFlow.value = cardGalleryData.allCardLikes
    }
}