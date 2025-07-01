package luke.koz.search.model

import luke.koz.domain.model.CardGalleryEntry
import luke.koz.presentation.state.SearchState

internal data class SearchScreenContentUiState(
    val query: String,
    val searchState: SearchState,
    val combinedResults: List<CardGalleryEntry>,
    val showFilters: Boolean,
    val showExactMatches: Boolean,
    val showApproximateMatches: Boolean,
    val isSearchBarActive: Boolean
)