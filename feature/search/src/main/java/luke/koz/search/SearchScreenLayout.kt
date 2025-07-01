package luke.koz.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import luke.koz.search.components.RenderSearchState
import luke.koz.search.components.FilterToggles
import luke.koz.search.components.SearchGalleryBar
import luke.koz.search.model.SearchScreenContentActions
import luke.koz.search.model.SearchScreenContentUiState

@Composable
internal fun SearchScreenLayout(
    uiState: SearchScreenContentUiState,
    actions: SearchScreenContentActions,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            SearchGalleryBar(
                query = uiState.query,
                onQueryChange = actions.onQueryChange,
                onSearch = { KeyboardActions(onSearch = { defaultKeyboardAction(ImeAction.Done) }) },
                onClearQuery = actions.onClearQuery,
                onNavigateBack = actions.onPopBackStack,
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.size(4.dp))

            OutlinedIconButton(
                onClick = { actions.onToggleFiltersMatches(uiState.showFilters) },
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterVertically)
                    .offset(y = 3.dp)// this is not a concrete number; fixes `SearchBar` and `OutlinedIconButton` differently defining `Alignment.CenterVertically`
            ) {
                Icon(
                    painter = painterResource(luke.koz.presentation.R.drawable.outline_filter_list_24),
                    contentDescription = if (uiState.showFilters) "Hide filters" else "Show filters"
                )
            }
        }

        if (uiState.showFilters) {
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

        HorizontalDivider(Modifier.padding(vertical = 8.dp))

        RenderSearchState(
            searchState = uiState.searchState,
            onCardClick = actions.onCardClick,
            imageLoader = imageLoader,
            combinedResults = uiState.combinedResults
        )
    }
}
