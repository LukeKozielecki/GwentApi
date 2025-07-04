package luke.koz.domain.cardgallery

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import luke.koz.domain.NetworkConnectivityChecker
import luke.koz.domain.model.CardGalleryData
import luke.koz.domain.repository.AuthStatusRepository
import luke.koz.domain.repository.CardGalleryRepository
import luke.koz.domain.repository.UserLikesDataSource

class RefreshCardGalleryDataUseCaseImpl(
    private val cardGalleryRepository: CardGalleryRepository,
    private val userLikesDataSource: UserLikesDataSource,
    private val authStatusRepository: AuthStatusRepository,
    private val networkConnectivityChecker: NetworkConnectivityChecker
) : RefreshCardGalleryDataUseCase {

    override suspend fun invoke(forceRefreshCards: Boolean): CardGalleryData {
        val currentUser = authStatusRepository.observeCurrentUser().first()
        val currentUserId = currentUser?.id

        val rawCards = cardGalleryRepository.getAllCards(forceRefreshCards).first()

        val likedCardIds = currentUserId?.let {
            userLikesDataSource.getLikedCardIdsForUser(it)
        } ?: emptySet()

        val allCardLikes = userLikesDataSource.getLikesForAllCards()

        return CardGalleryData(rawCards, likedCardIds, allCardLikes)
    }

    override fun observeAuthAndInternetChanges(): Flow<Pair<String?, Boolean>> {
        return combine(
            authStatusRepository.observeCurrentUser(),
            networkConnectivityChecker.observeInternetAvailability()
        ) { authUserModel, isInternetAvailable ->
            Pair(authUserModel?.id, isInternetAvailable)
        }
    }
}