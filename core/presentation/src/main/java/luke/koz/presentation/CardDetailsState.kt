package luke.koz.presentation

import luke.koz.domain.model.CardDetailsEntry

sealed class CardDetailsState {
    object Empty : CardDetailsState()
    object Loading : CardDetailsState()
    data class Success(val cards: List<CardDetailsEntry>) : CardDetailsState()
    data class Error(val message: String) : CardDetailsState()
}