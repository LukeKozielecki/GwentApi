package luke.koz.presentation.state

import luke.koz.domain.model.CardSearchResult

sealed class SearchState {
    object Idle : SearchState()
    object Loading : SearchState()
    object Empty : SearchState()
    data class Success(val searchResults: CardSearchResult) : SearchState()
    data class Error(val message: String) : SearchState()
}
