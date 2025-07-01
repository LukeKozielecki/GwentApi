package luke.koz.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import luke.koz.presentation.state.SearchState
import luke.koz.search.components.FilterToggles
import luke.koz.search.components.RenderSearchState
import luke.koz.search.components.SearchScreenTopAppBar
import luke.koz.search.model.SearchScreenContentActions
import luke.koz.search.model.SearchScreenContentUiState

/**
 * An internal composable that defines the overall layout structure of the Search Screen.
 *
 * This composable acts as the primary UI layer for the search feature, orchestrating
 * the arrangement of its sub-components: the dynamic [SearchScreenTopAppBar],
 * the [FilterToggles] section, and the [RenderSearchState] area for displaying
 * search results or status messages.
 *
 * @param uiState The current UI state ([SearchScreenContentUiState]) containing all
 * the data and flags required to render the search screen's various elements.
 * @param actions The set of actions ([SearchScreenContentActions]) that define how
 * user interactions are communicated to the ViewModel.
 * @param imageLoader An [ImageLoader] instance used for loading and displaying
 * images within the search results.
 * @param modifier An optional [Modifier] to be applied to the root [Column] of this composable.
 */
@Composable
internal fun SearchScreenLayout(
    uiState: SearchScreenContentUiState,
    actions: SearchScreenContentActions,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect (Unit){
        if (uiState.searchState == SearchState.Idle) {
            actions.onToggleSearchBarActive(true)
        }
    }
    Scaffold(
        topBar = {
            SearchScreenTopAppBar(
                uiState = uiState,
                actions = actions,
                focusManager = focusManager,
                focusRequester = focusRequester
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            if (uiState.showFilters) {
                HorizontalDivider(Modifier.padding(vertical = 2.dp))
                FilterToggles(
                    showExactMatches = uiState.showExactMatches,
                    showApproximateMatches = uiState.showApproximateMatches,
                    onToggleExactMatches = actions.onToggleExactMatches,
                    onToggleApproximateMatches = actions.onToggleApproximateMatches,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            HorizontalDivider(Modifier.padding(vertical = 2.dp))

            RenderSearchState(
                searchState = uiState.searchState,
                onCardClick = actions.onCardClick,
                imageLoader = imageLoader,
                combinedResults = uiState.combinedResults
            )
        }

    }
}
