package luke.koz.domain.cardgallery

import luke.koz.domain.repository.CardGalleryRepository

class ToggleCardLikeUseCaseImpl(
    private val cardGalleryRepository: CardGalleryRepository
) : ToggleCardLikeUseCase {
    override suspend fun invoke(userId: String, cardId: Int, isCurrentlyLiked: Boolean): Result<Unit> {
        return cardGalleryRepository.toggleCardLike(userId, cardId, !isCurrentlyLiked)
    }
}