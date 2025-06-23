package luke.koz.domain.repository

import kotlinx.coroutines.flow.Flow
import luke.koz.domain.model.CardGalleryEntry

/**
 * Interface for the Card Gallery Repository, defining the contract for data operations
 * related to Gwent cards.
 */
interface CardGalleryRepository {

    /**
     * Retrieves a single card by its ID.
     * The implementation should handle fetching from local cache first, then remote if not found or stale.
     *
     * @param cardId The unique identifier of the card.
     * @return A [Flow] emitting the [CardGalleryEntry] for the specified card.
     */
    fun getCard(cardId: Int): Flow<CardGalleryEntry>

    /**
     * Retrieves a list of cards that match a given query string.
     * The implementation should fetch cards that [CardGalleryEntry.name] matches query.
     *
     * @param query The search query string.
     * @return A [Flow] emitting a [List] of [CardGalleryEntry] that match the query.
     */
    fun getCardByQuery(query: String): Flow<List<CardGalleryEntry>>

    /**
     * Retrieves all cards, with an option to force a refresh from the remote source.
     * The implementation should emit cached data first, then potentially refresh and emit updated data.
     * It also combines card data with user like status.
     *
     * @param forceRefresh If true, forces a fetch from the remote data source.
     * @return A [Flow] emitting a [List] of [CardGalleryEntry], including like status.
     */
    fun getAllCards(forceRefresh: Boolean = false): Flow<List<CardGalleryEntry>>

    /**
     * Retrieves the set of card IDs liked by a specific user.
     * This is used to determine ui state that should be displayed to the user.
     *
     * @param userId The ID of the user.
     * @return A [Set] of [Int] representing the IDs of cards liked by the user.
     */
    suspend fun getLikedCardIdsForUser(userId: String): Set<Int>

    /**
     * Retrieves a map of all card IDs to the set of user IDs who liked them.
     *
     * @return A [Map] where the key is the card ID ([Int]) and the value is a [Set] of user IDs ([String]).
     */
    suspend fun getLikesForAllCards(): Map<Int, Set<String>>

    /**
     * Toggles the like status for a specific card by a specific user.
     *
     * @param userId The ID of the user performing the like/unlike.
     * @param cardId The ID of the card to toggle the like status for.
     * @param isLiking True if the user is liking the card, false if unliking.
     * @return A [Result] indicating success or failure of the operation.
     */
    suspend fun toggleCardLike(userId: String, cardId: Int, isLiking: Boolean): Result<Unit>
}