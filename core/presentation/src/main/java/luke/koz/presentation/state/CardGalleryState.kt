package luke.koz.presentation.state

import luke.koz.domain.model.CardGalleryEntry

sealed class CardGalleryState {
    object Empty : CardGalleryState()
    object Loading : CardGalleryState()
    data class Success(val cards: List<CardGalleryEntry>) : CardGalleryState()
    data class Error(val message: String) : CardGalleryState()
}
