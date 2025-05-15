package luke.koz.gwentapi.ui.state

import luke.koz.gwentapi.domain.model.CardGalleryEntry

sealed class CardState {
    object Empty : CardState()
    object Loading : CardState()
    data class Success(val cards: List<CardGalleryEntry>) : CardState()
    data class Error(val message: String) : CardState()
}