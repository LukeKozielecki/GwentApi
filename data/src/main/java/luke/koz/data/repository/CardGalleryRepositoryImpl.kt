package luke.koz.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
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
        val cachedCards = getCachedCards()

        // If forceRefresh is true or cache is empty, attempt to fetch from remote and update local
        if (forceRefresh || cachedCards.isEmpty()) {
            try {
                handleRemoteRefreshSuccess(this)
            } catch (e: Exception) {
                handleRemoteRefreshFailure(cachedCards, e, this)
            }
        } else {
            // If no refresh is needed, just emit the existing cached cards combined with likes
            emitCombinedCards(cachedCards, this)
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
            Log.w("RepoDebug", "No internet connection. Cannot toggle like for card $cardId")
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

    /**
     * Retrieves the current list of cached cards from the local data source.
     */
    private suspend fun getCachedCards(): List<CardGalleryEntry> {
        return local.getAllCards().first().map { it.toCardGalleryEntry() }
    }

    /**
     * Handles the successful remote refresh of all cards.
     * Fetches from remote, updates local cache, then emits the combined, updated cards.
     *
     * @param collector The FlowCollector to emit items to.
     */
    private suspend fun handleRemoteRefreshSuccess(collector: FlowCollector<List<CardGalleryEntry>>) {
        Log.d("RepoDebug", "Attempting to refresh all cards from remote.")
        val remoteCards = remote.getAllCards()
        local.upsertCards(remoteCards.map { it.toCardEntity() })
        val updatedLocalCards = getCachedCards()
        emitCombinedCards(updatedLocalCards, collector)
    }

    /**
     * Handles the failure of remote refresh.
     * Logs the error and emits the existing cached cards combined with likes.
     *
     * @param cachedCards The list of cards currently in cache.
     * @param e The exception that occurred during remote fetch.
     * @param collector The FlowCollector to emit items to.
     */
    private suspend fun handleRemoteRefreshFailure(
        cachedCards: List<CardGalleryEntry>,
        e: Exception,
        collector: FlowCollector<List<CardGalleryEntry>>
    ) {
        Log.e("RepoDebug", "Remote fetch failed for all cards", e)
        emitCombinedCards(cachedCards, collector)
    }

    /**
     * Combines a given list of cards with user like status and emits them.
     *
     * @param cards The list of cards to combine and emit.
     * @param collector The FlowCollector to emit items to.
     */
    private suspend fun emitCombinedCards(
        cards: List<CardGalleryEntry>,
        collector: FlowCollector<List<CardGalleryEntry>>
    ) {
        val userId = auth.currentUser?.uid ?: ""
        val allLikes = userLikesDataSource.getLikesForAllCards()
        val combinedCards = cards.map { card ->
            card.copy(
                isLiked = allLikes[card.id]?.contains(userId) ?: false,
                likeCount = allLikes[card.id]?.size ?: 0
            )
        }
        collector.emit(combinedCards)
    }
}