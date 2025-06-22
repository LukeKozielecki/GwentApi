package luke.koz.presentation

import luke.koz.domain.model.CardDetailsEntry
import luke.koz.domain.model.CardGalleryEntry

sealed class CardState {
    object Empty : CardState()
    object Loading : CardState()
    data class Success(val cards: List<CardGalleryEntry>) : CardState()
    data class Error(val message: String) : CardState()
}

sealed class CardDetailsState {
    object Empty : CardDetailsState()
    object Loading : CardDetailsState()
    data class Success(val cards: List<CardDetailsEntry>) : CardDetailsState()
    data class Error(val message: String) : CardDetailsState()
}