package luke.koz.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import coil3.ImageLoader
import kotlinx.coroutines.launch
import luke.koz.search.model.SearchScreenContentActions
import luke.koz.search.model.SearchScreenContentUiState
import luke.koz.search.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onCardClick : (Int) -> Unit,
    onPopBackStack: () -> Unit,
    imageLoader: ImageLoader
) {
    val scope = rememberCoroutineScope()
    SearchScreenLayout(
        uiState = SearchScreenContentUiState(
            query = viewModel.query.collectAsState().value,
            searchState = viewModel.searchState.collectAsState().value,
            combinedResults = viewModel.combinedResults.collectAsState().value,
            showFilters = viewModel.showFilters.collectAsState().value,
            showExactMatches = viewModel.showExactMatches.collectAsState().value,
            showApproximateMatches = viewModel.showApproximateMatches.collectAsState().value,
            isSearchBarActive = viewModel.isSearchBarActive.collectAsState().value
        ),
        actions = SearchScreenContentActions(
            onCardClick = onCardClick,
            onQueryChange = { viewModel.updateQuery(it) },
            onSearch = { },
            onClearQuery = { viewModel.updateQuery("") },
            onPopBackStack = onPopBackStack,
            onToggleFiltersMatches = { viewModel.toggleFilters(it) },
            onToggleExactMatches = {
                scope
                    .launch {
                        viewModel.toggleExactMatches(it)
                    }
            },
            onToggleApproximateMatches = {
                scope
                    .launch {
                        viewModel.toggleApproximateMatches(it)
                    }
            },
            onToggleSearchBarActive = { viewModel.toggleSearchBarActive(it) },
        ),
        imageLoader = imageLoader
    )
}

