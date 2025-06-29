package luke.koz.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import luke.koz.search.components.CustomSearchBar
import luke.koz.search.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onCardClick : (Int) -> Unit,
    closeSearch: () -> Unit,
    imageLoader: ImageLoader
) {
    val searchState by viewModel.searchState.collectAsState()
    val query by viewModel.query.collectAsState()
    val combinedResults by viewModel.combinedResults.collectAsState()
    val showExact by viewModel.showExactMatches.collectAsState()
    val showApproximate by viewModel.showApproximateMatches.collectAsState()
    Scaffold { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            CustomSearchBar(
                query = query,
                searchState = searchState,
                onQueryChange = {
                    viewModel.updateQuery(it)
                },
                onClearQuery = { viewModel.updateQuery("") },
                onClose = closeSearch,
                imageLoader = imageLoader,
                combinedResults = combinedResults,
                onCardClick = onCardClick,
                showExactMatches = showExact,
                showApproximateMatches = showApproximate,
                onToggleExactMatches = { viewModel.toggleExactMatches(it) },
                onToggleApproximateMatches = { viewModel.toggleApproximateMatches(it) }
            )
        }
    }
}