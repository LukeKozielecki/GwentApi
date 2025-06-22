package luke.koz.domain.model

import kotlinx.coroutines.flow.Flow

interface UserLikesDataSource {
    suspend fun toggleCardLike(userId: String, cardId: Int, isLiking: Boolean)
    suspend fun getLikedCardIdsForUser(userId: String): Set<Int>
    suspend fun getLikesForAllCards(): Map<Int, Set<String> >
    fun observeLikesForAllCards(): Flow<Map<Int, Set<String>>>
}
