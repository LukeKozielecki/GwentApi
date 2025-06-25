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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.domain.repository.AuthStatusRepository
import luke.koz.domain.repository.CardGalleryRepository
import luke.koz.domain.repository.UserLikesDataSource
import luke.koz.presentation.CardState

class CardGalleryViewModel (
    private val repository: CardGalleryRepository,
    private val userLikesDataSource: UserLikesDataSource,
    private val authStatusRepository: AuthStatusRepository
) : ViewModel (){
    private val _cardState = mutableStateOf<CardState>(CardState.Empty)
    val cardState: State<CardState> = _cardState

    private val _rawCardsFlow = MutableStateFlow<List<CardGalleryEntry>>(emptyList())
    private val _likedCardIdsFlow = MutableStateFlow<Set<Int>>(emptySet())
    private val _allCardLikesFlow = MutableStateFlow<Map<Int, Set<String>>>(emptyMap())
    private val _isInitialLoadingComplete = MutableStateFlow(false)

    private val _currentUserId = MutableStateFlow<String?>(null)

    init {
        _cardState.value = CardState.Loading

        _rawCardsFlow
            .onEach { Log.d("FlowDebug", "Raw cards updated: ${it.size} entries") }
            .launchIn(viewModelScope)

        _likedCardIdsFlow
            .onEach { Log.d("FlowDebug", "Liked IDs updated: $it") }
            .launchIn(viewModelScope)

        _allCardLikesFlow
            .onEach { Log.d("FlowDebug", "All likes updated: $it") }
            .launchIn(viewModelScope)

        combine(
            _rawCardsFlow,
            _likedCardIdsFlow,
            _allCardLikesFlow,
            _isInitialLoadingComplete
        ) { rawCards, likedIds, allLikes, isInitialLoadingComplete ->
            val cardsWithLikes = rawCards.map { card ->
                card.copy(
                    isLiked = likedIds.contains(card.id),
                    likeCount = allLikes[card.id]?.size ?: 0
                )
            }
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

        authStatusRepository.observeCurrentUser()
            .onEach { authUserModel ->
                _currentUserId.value = authUserModel?.id
                Log.d("CardGalleryVM", "Auth status changed via repo. User: ${authUserModel?.email ?: "null"}")
                refreshLikesForCurrentUser(authUserModel?.id)
            }
            .launchIn(viewModelScope)
        getAllCards()
    }

    fun getAllCards(forceRefresh: Boolean = false) {
        Log.d("CardGalleryVM", "viewModelScope.launch: getAllCards")
        viewModelScope.launch {
            _cardState.value = CardState.Loading
            _isInitialLoadingComplete.value = false

            try {
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

            _likedCardIdsFlow.value = currentUserId?.let {
                userLikesDataSource.getLikedCardIdsForUser(it).also { likedIds ->
                    Log.d("CardGalleryVM", "Liked IDs fetched: ${likedIds.size}")
                }
            } ?: emptySet()

            userLikesDataSource.getLikesForAllCards().also { allLikes ->
                Log.d("CardGalleryVM", "All likes fetched: ${allLikes.size}")
                _allCardLikesFlow.value = allLikes
            }
        } catch (e: Exception) {
            Log.e("CardGalleryVM", "Error refreshing likes: ${e.message}", e)
            throw e
        }
    }

    fun toggleLike(cardId: Int, isCurrentlyLiked: Boolean) {
        val userId = _currentUserId.value ?: run {
            _cardState.value = CardState.Error("Authentication required")
            return
        }

        viewModelScope.launch {
            // Optimistic update
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

            // Database operation
            try {
                repository.toggleCardLike(userId, cardId, !isCurrentlyLiked)
            } catch (e: Exception) {
                _likedCardIdsFlow.update { ids ->
                    if (isCurrentlyLiked) ids + cardId else ids - cardId
                }
                _allCardLikesFlow.update { likesMap ->
                    likesMap.toMutableMap().apply {
                        val users = getOrPut(cardId) { mutableSetOf() }.toMutableSet()
                        if (isCurrentlyLiked) users.add(userId) else users.remove(userId)
                        if (users.isEmpty()) remove(cardId) else put(cardId, users)
                    }
                }
                _cardState.value = CardState.Error("Like update failed: ${e.message}")
            }
        }
    }
}