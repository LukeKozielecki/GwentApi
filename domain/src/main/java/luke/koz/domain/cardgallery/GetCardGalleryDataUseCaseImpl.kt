package luke.koz.domain.cardgallery

import kotlinx.coroutines.flow.Flow
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.domain.repository.AuthStatusRepository
import luke.koz.domain.repository.CardGalleryRepository
import luke.koz.domain.repository.UserLikesDataSource

class GetCardGalleryDataUseCaseImpl(
    private val cardGalleryRepository: CardGalleryRepository,
    private val userLikesDataSource: UserLikesDataSource,
    private val authStatusRepository: AuthStatusRepository
) : GetCardGalleryDataUseCase {

    override operator fun invoke(): Flow<List<CardGalleryEntry>> {
        return cardGalleryRepository.getAllCards(forceRefresh = false)
    }
}