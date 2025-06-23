package luke.koz.domain.repository

import kotlinx.coroutines.flow.Flow
import luke.koz.domain.model.CardDetailsEntry

/**
 * Interface for the Card Details Repository, defining the contract for fetching
 * details of given card.
 */
interface CardDetailsRepository {
    fun getCardDetails(cardId: Int): Flow<CardDetailsEntry>
}