package luke.koz.gwentapi.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import luke.koz.gwentapi.data.datasource.UserLikesDataSource
import luke.koz.gwentapi.data.repository.CardGalleryRepository
import luke.koz.gwentapi.domain.model.CardGalleryEntry
import luke.koz.gwentapi.ui.state.CardState

class CardGalleryViewModel (
    private val repository: CardGalleryRepository,
    private val userLikesDataSource: UserLikesDataSource,
    private val auth: FirebaseAuth
) : ViewModel (){
    private val _cardState = mutableStateOf<CardState>(CardState.Empty)
    val cardState: State<CardState> = _cardState

    private val _rawCardsFlow = MutableStateFlow<List<CardGalleryEntry>>(emptyList())
    private val _likedCardIdsFlow = MutableStateFlow<Set<Int>>(emptySet())
    private val _allCardLikesFlow = MutableStateFlow<Map<Int, Set<String>>>(emptyMap())

    init {
        // Always start with Loading state
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
            _allCardLikesFlow
        ) { rawCards, likedIds, allLikes ->
            rawCards.map { card ->
                card.copy(
                    isLiked = likedIds.contains(card.id),
                    likeCount = allLikes[card.id]?.size ?: 0
                )
            }
        }
            .flowOn(Dispatchers.Default)
            .onEach { combined ->
                _cardState.value = when {
                    combined.isEmpty() -> CardState.Empty
                    else -> CardState.Success(combined)
                }
            }
            .catch { e ->
                _cardState.value = CardState.Error("Error: ${e.message}")
            }
            .launchIn(viewModelScope)

        getAllCards()
    }

    fun getAllCards(forceRefresh: Boolean = false) {
        Log.d("CardGalleryVM", "viewModelScope.launch: getAllCards")
        viewModelScope.launch {
            _cardState.value = CardState.Loading
            try {
                // Fetch cards
                Log.d("CardGalleryVM", "Fetch cards first")
                val cardsJob = launch {
                    repository.getAllCards(forceRefresh).collect { cards ->
                        Log.d("CardGalleryVM", "Cards collected: ${cards.size}")
                        _rawCardsFlow.value = cards
                    }
                }

                // Fetch likes
                Log.d("CardGalleryVM", "Fetch likes")
                val likesJob = launch {
                    val currentUserId = auth.currentUser?.uid
                    _likedCardIdsFlow.value = currentUserId?.let {
                        userLikesDataSource.getLikedCardIdsForUser(it).also {
                            Log.d("CardGalleryVM", "Liked IDs fetched: ${it.size}")
                        }
                    } ?: emptySet()

                    userLikesDataSource.getLikesForAllCards().also { allLikes ->
                        Log.d("CardGalleryVM", "All likes fetched: ${allLikes.size}")
                        _allCardLikesFlow.value = allLikes
                    }
                }

                cardsJob.join()
                likesJob.join()

            } catch (e: Exception) {
                _cardState.value = CardState.Error(e.message ?: "Failed to load cards")
            }
        }
    }

    fun toggleLike(cardId: Int, isCurrentlyLiked: Boolean) {
        val userId = auth.currentUser?.uid ?: run {
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