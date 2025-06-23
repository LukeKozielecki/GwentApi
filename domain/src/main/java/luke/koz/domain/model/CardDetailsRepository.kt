package luke.koz.domain.model

import kotlinx.coroutines.flow.Flow

/**
 * Interface for the Card Details Repository, defining the contract for fetching
 * details of given card.
 */
interface CardDetailsRepository {
    fun getCardDetails(cardId: Int): Flow<CardDetailsEntry>
}