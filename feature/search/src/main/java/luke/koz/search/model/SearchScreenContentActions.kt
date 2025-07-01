package luke.koz.search.model

internal data class SearchScreenContentActions (
    val onCardClick: (Int) -> Unit,
    val onQueryChange: (String) -> Unit,
    val onSearch: () -> Unit,
    val onClearQuery: () -> Unit,
    val onPopBackStack: () -> Unit,
    val onToggleFiltersMatches: (Boolean) -> Unit,
    val onToggleExactMatches: (Boolean) -> Unit,
    val onToggleApproximateMatches: (Boolean) -> Unit,
    val onToggleSearchBarActive: (Boolean) -> Unit
)