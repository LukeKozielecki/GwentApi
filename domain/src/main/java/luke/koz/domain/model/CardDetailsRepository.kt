package luke.koz.domain.model

import kotlinx.coroutines.flow.Flow

//todo: workname, change back to CardDetailsRepository and rename implementation to + Impl
interface CardDetailsRepository {
    fun getCardDetails(cardId: Int): Flow<CardDetailsEntry>
}