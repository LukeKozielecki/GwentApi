package luke.koz.domain.cardgallery

interface ToggleCardLikeUseCase {
    suspend fun invoke(userId: String, cardId: Int, isCurrentlyLiked: Boolean): Result<Unit>
}