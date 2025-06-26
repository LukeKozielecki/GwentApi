package luke.koz.domain.cardgallery

import kotlinx.coroutines.flow.Flow
import luke.koz.domain.model.CardDetailsEntry

interface GetCardDetailUseCase {
    operator fun invoke(cardId: Int): Flow<CardDetailsEntry>
}