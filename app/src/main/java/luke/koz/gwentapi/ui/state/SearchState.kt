package luke.koz.gwentapi.ui.state

import luke.koz.gwentapi.domain.model.CardGalleryEntry

sealed class SearchState {
    object Idle : SearchState()
    object Loading : SearchState()
    object Empty : SearchState()
    data class Success(val results: List<CardGalleryEntry>) : SearchState()
    data class Error(val message: String) : SearchState()
}
