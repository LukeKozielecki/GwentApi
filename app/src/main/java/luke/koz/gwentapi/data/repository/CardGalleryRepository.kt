package luke.koz.gwentapi.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import luke.koz.gwentapi.data.datasource.CardLocalDataSource
import luke.koz.gwentapi.data.datasource.CardRemoteDataSource
import luke.koz.gwentapi.data.datasource.UserLikesDataSource
import luke.koz.gwentapi.data.mapper.toCardGalleryEntry
import luke.koz.gwentapi.data.mapper.toCardEntity
import luke.koz.gwentapi.domain.model.CardGalleryEntry

class CardGalleryRepository(
    private val remote: CardRemoteDataSource,
    private val local: CardLocalDataSource,
    val userLikesDataSource: UserLikesDataSource,
    private val auth: FirebaseAuth
) {
    fun getCard(cardId: Int): Flow<CardGalleryEntry> = flow {
        local.getCardById(cardId).collect { cached ->
            cached?.let { emit(it.toCardGalleryEntry()) }

            if (cached == null) {
                try {
                    Log.d("HarassApi", "Data was requested from remote source")
                    val cardDto = remote.getCardById(cardId)
                    local.upsertCard(cardDto.toCardEntity())
                } catch (e: Exception) {
                    Log.e("RepoDebug", "API error", e)
                    throw e
                }
            }

            local.getCardById(cardId).collect { updated ->
                updated?.let { emit(it.toCardGalleryEntry()) }
            }
        }
    }.flowOn(Dispatchers.IO)

    fun getCardByQuery(query: String) : Flow<List<CardGalleryEntry>> = flow {
        local.getCardByQuery(query).collect { cachedList ->
            val domainList = cachedList.map { it.toCardGalleryEntry() }
            emit(domainList)
        }
    }.flowOn(Dispatchers.IO)

    fun getAllCards(forceRefresh: Boolean = false): Flow<List<CardGalleryEntry>> = flow {
        val cachedCards = local.getAllCards().first().map { it.toCardGalleryEntry() }

        emit(cachedCards)

        if (forceRefresh || cachedCards.isEmpty()) {
            try {
                val remoteCards = remote.getAllCards()
                local.upsertCards(remoteCards.map { it.toCardEntity() })
            } catch (e: Exception) {
                Log.e("RepoDebug", "Remote fetch failed", e)
            }
        }

        val cardsFlow = local.getAllCards().map { entities ->
            entities.map { it.toCardGalleryEntry() }
        }

        val likesFlow  = userLikesDataSource.observeLikesForAllCards()

        cardsFlow.combine(likesFlow) { cards, allLikes ->
            val userId = auth.currentUser?.uid ?: ""
            cards.map { card ->
                card.copy(
                    isLiked = allLikes[card.id]?.contains(userId) ?: false,
                    likeCount = allLikes[card.id]?.size ?: 0
                )
            }
        }.collect { combined ->
            emit(combined)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getLikedCardIdsForUser(userId: String): Set<Int> {
        return userLikesDataSource.getLikedCardIdsForUser(userId)
    }

    suspend fun getLikesForAllCards(): Map<Int, Set<String>> {
        return userLikesDataSource.getLikesForAllCards()
    }

    suspend fun toggleCardLike(userId: String, cardId: Int, isLiking: Boolean): Result<Unit> {
        return try {
            userLikesDataSource.toggleCardLike(userId, cardId, isLiking)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("RepoDebug", "Failed to toggle like for card $cardId by user $userId", e)
            Result.failure(e)
        }
    }
}