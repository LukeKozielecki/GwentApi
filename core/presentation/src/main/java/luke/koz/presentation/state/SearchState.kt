package luke.koz.presentation.state

import luke.koz.domain.model.CardGalleryEntry

sealed class SearchState {
    object Idle : SearchState()
    object Loading : SearchState()
    object Empty : SearchState()
    data class Success(val results: List<CardGalleryEntry>) : SearchState()
    data class Error(val message: String) : SearchState()
}
