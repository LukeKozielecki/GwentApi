package luke.koz.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import luke.koz.data.datasource.CardLocalDataSource
import luke.koz.data.datasource.CardRemoteDataSource
import luke.koz.data.mapper.toCardEntity
import luke.koz.data.mapper.toCardGalleryEntry
import luke.koz.domain.NetworkConnectivityChecker
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.domain.repository.CardGalleryRepository
import luke.koz.domain.repository.UserLikesDataSource
import java.io.IOException

/**
 * Implementation of the [CardGalleryRepository] interface. This class is part of the
 * data layer and is responsible for orchestrating data flow from various sources
 * (remote API, local database, user likes data source) and transforming it into
 * domain-layer models.
 *
 * @param remote The data source for fetching card data from a remote API.
 * @param local The data source for local caching of card data.
 * @param userLikesDataSource The data source for managing user likes on cards.
 * @param auth Firebase Authentication instance for retrieving current user ID.
 */
class CardGalleryRepositoryImpl(
    private val remote: CardRemoteDataSource,
    private val local: CardLocalDataSource,
    private val userLikesDataSource: UserLikesDataSource,
    private val networkConnectivityChecker: NetworkConnectivityChecker,
    private val auth: FirebaseAuth
) : CardGalleryRepository {

    private val isInternetAvailable = networkConnectivityChecker.observeInternetAvailability()

    /**
     * @see CardGalleryRepository.getCard
     */
    override fun getCard(cardId: Int): Flow<CardGalleryEntry> = flow {
        local.getCardById(cardId).collect { cached ->
            cached?.let { emit(it.toCardGalleryEntry()) }

            if (cached == null) {
                try {
                    Log.d("HarassApi", "Data was requested from remote source for cardId: $cardId")
                    val cardDto = remote.getCardById(cardId)
                    local.upsertCard(cardDto.toCardEntity())
                } catch (e: Exception) {
                    Log.e("RepoDebug", "API error fetching card $cardId", e)
                    throw e
                }
            }

            local.getCardById(cardId).collect { updated ->
                updated?.let { emit(it.toCardGalleryEntry()) }
            }
        }
    }.flowOn(Dispatchers.IO)

    /**
     * @see CardGalleryRepository.getCardByQuery
     */
    override fun getCardByQuery(query: String): Flow<List<CardGalleryEntry>> = flow {
        local.getCardByQuery(query).collect { cachedList ->
            val domainList = cachedList.map { it.toCardGalleryEntry() }
            emit(domainList)
        }
    }.flowOn(Dispatchers.IO)

    /**
     * @see CardGalleryRepository.getAllCards
     */
    override fun getAllCards(forceRefresh: Boolean): Flow<List<CardGalleryEntry>> = flow {
        // Emit cached cards immediately for a fast initial display
        val cachedCards = local.getAllCards().first().map { it.toCardGalleryEntry() }

        // If forceRefresh is true or cache is empty, attempt to fetch from remote and update local
        if (forceRefresh || cachedCards.isEmpty()) {
            try {
                Log.d("RepoDebug", "Attempting to refresh all cards from remote.")
                val remoteCards = remote.getAllCards()
                local.upsertCards(remoteCards.map { it.toCardEntity() })
                // After successful remote fetch and upsert, the local database is updated
                val updatedLocalCards = local.getAllCards().first().map { it.toCardGalleryEntry() }
                val userId = auth.currentUser?.uid ?: ""
                val allLikes = userLikesDataSource.getLikesForAllCards()
                val combinedUpdatedCards = updatedLocalCards.map { card ->
                    card.copy(
                        isLiked = allLikes[card.id]?.contains(userId) ?: false,
                        likeCount = allLikes[card.id]?.size ?: 0
                    )
                }
                emit(combinedUpdatedCards)
            } catch (e: Exception) {
                Log.e("RepoDebug", "Remote fetch failed for all cards", e)
                // If remote fetch fails, still emit the cached cards if available
                val userId = auth.currentUser?.uid ?: ""
                val allLikes = userLikesDataSource.getLikesForAllCards()
                val combinedCachedCards = cachedCards.map { card ->
                    card.copy(
                        isLiked = allLikes[card.id]?.contains(userId) ?: false,
                        likeCount = allLikes[card.id]?.size ?: 0
                    )
                }
                emit(combinedCachedCards)
            }
        } else {
            // If no refresh is needed, just emit the existing cached cards combined with likes
            val userId = auth.currentUser?.uid ?: ""
            val allLikes = userLikesDataSource.getLikesForAllCards()
            val combinedCachedCards = cachedCards.map { card ->
                card.copy(
                    isLiked = allLikes[card.id]?.contains(userId) ?: false,
                    likeCount = allLikes[card.id]?.size ?: 0
                )
            }
            emit(combinedCachedCards)
        }
    }.flowOn(Dispatchers.IO)

    /**
     * @see CardGalleryRepository.getLikedCardIdsForUser
     */
    override suspend fun getLikedCardIdsForUser(userId: String): Set<Int> {
        return userLikesDataSource.getLikedCardIdsForUser(userId)
    }

    /**
     * @see CardGalleryRepository.getLikesForAllCards
     */
    override suspend fun getLikesForAllCards(): Map<Int, Set<String>> {
        return userLikesDataSource.getLikesForAllCards()
    }

    /**
     * @see CardGalleryRepository.toggleCardLike
     */
    override suspend fun toggleCardLike(userId: String, cardId: Int, isLiking: Boolean): Result<Unit> {
        if (!isInternetAvailable.first()) {
            Log.w("RepoDebug", "No internet connection. Cannot toggle like for card $cardId.")
            return Result.failure(IOException("No internet connection to toggle like."))
        }
        return try {
            userLikesDataSource.toggleCardLike(userId, cardId, isLiking)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("RepoDebug", "Failed to toggle like for card $cardId by user $userId", e)
            Result.failure(e)
        }
    }
}