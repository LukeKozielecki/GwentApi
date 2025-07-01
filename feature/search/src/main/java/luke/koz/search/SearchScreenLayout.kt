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
