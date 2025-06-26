package luke.koz.domain.cardgallery

import kotlinx.coroutines.flow.Flow
import luke.koz.domain.model.CardDetailsEntry
import luke.koz.domain.repository.CardDetailsRepository

class GetCardDetailUseCaseImpl(
    private val cardGalleryRepository: CardDetailsRepository
) : GetCardDetailUseCase {
    override operator fun invoke(cardId: Int): Flow<CardDetailsEntry> {
        return cardGalleryRepository.getCardDetails(cardId)
    }
}