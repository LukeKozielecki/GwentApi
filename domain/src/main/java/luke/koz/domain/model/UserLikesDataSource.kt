package luke.koz.domain.model

import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for managing user likes on cards.
 */
interface UserLikesDataSource {

    /**
     * Toggles the like status for a specific card by a specific user.
     * If [isLiking] is true, the card is marked as liked by the user.
     * If [isLiking] is false, the card is marked as unliked by the user.
     *
     * @param userId The ID of the user performing the like/unlike.
     * @param cardId The ID of the card to toggle the like status for.
     * @param isLiking True if the user is liking the card, false if unliking.
     */
    suspend fun toggleCardLike(userId: String, cardId: Int, isLiking: Boolean)

    /**
     * Retrieves a set of card IDs that a specific user has liked.
     *
     * @param userId The unique identifier of the user.
     * @return A [Set] of [Int] representing the IDs of cards liked by the user.
     * @throws Exception if the data cannot be fetched.
     */
    suspend fun getLikedCardIdsForUser(userId: String): Set<Int>

    /**
     * Retrieves a map containing all card IDs and the set of user IDs who have liked each card.
     * This is a one-time fetch operation.
     *
     * @return A [Map] where the key is the card ID ([Int]) and the value is a [Set] of user IDs ([String]).
     */
    suspend fun getLikesForAllCards(): Map<Int, Set<String>>

    /**
     * Observes changes to the like status for all cards across all users.
     * This [Flow] emits a new map whenever there's a change in the like data.
     *
     * @return A [Flow] emitting A [Map] where the key is the card ID ([Int]) and the value is a [Set] of user IDs ([String]).
     * This map represents the current state of likes for all cards.
     */
    fun observeLikesForAllCards(): Flow<Map<Int, Set<String>>>
}
