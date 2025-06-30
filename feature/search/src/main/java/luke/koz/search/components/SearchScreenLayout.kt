package luke.koz.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.presentation.card.CardList
import luke.koz.presentation.state.SearchState
import luke.koz.presentation.statusscreen.ErrorStatusScreen
import luke.koz.presentation.statusscreen.LoadingStatusScreen
import luke.koz.presentation.statusscreen.NoDataStatusScreen
import luke.koz.presentation.statusscreen.SuccessStatusScreen

@Composable
fun SearchScreenLayout(
    query: String,
    searchState: SearchState,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onClose: () -> Unit,
    onCardClick: (Int) -> Unit,
    imageLoader: ImageLoader,
    combinedResults: List<CardGalleryEntry>,
    showExactMatches: Boolean,
    showApproximateMatches: Boolean,
    showFilters: Boolean,
    onToggleExactMatches: (Boolean) -> Unit,
    onToggleApproximateMatches: (Boolean) -> Unit,
    onToggleFiltersMatches: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            SearchGalleryBar(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = { KeyboardActions(onSearch = { defaultKeyboardAction(ImeAction.Done) }) },
                onClearQuery = onClearQuery,
                onNavigateBack = onClose,
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.size(4.dp))

            OutlinedIconButton(
                onClick = { onToggleFiltersMatches(showFilters) },
                modifier = Modifier
                    .size(48.dp)
                    .offset(y = (20).dp)//this is the best method to get approximate alignment for this i can get
            ) {
                Icon(
                    painter = painterResource(luke.koz.presentation.R.drawable.outline_filter_list_24),
                    contentDescription = if (showFilters) "Hide filters" else "Show filters"
                )
            }
        }

        if (showFilters) {
            SearchFilterToggles(
                showExactMatches = showExactMatches,
                showApproximateMatches = showApproximateMatches,
                onToggleExactMatches = onToggleExactMatches,
                onToggleApproximateMatches = onToggleApproximateMatches,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        HorizontalDivider(Modifier.padding(vertical = 8.dp))

        SearchContent(
            searchState = searchState,
            onCardClick = onCardClick,
            imageLoader = imageLoader,
            combinedResults = combinedResults
        )
    }
}

/**
 * Composable for displaying and toggling search filter options: Exact Matches and Approximate Matches.
 */
@Composable
private fun SearchFilterToggles(
    showExactMatches: Boolean,
    showApproximateMatches: Boolean,
    onToggleExactMatches: (Boolean) -> Unit,
    onToggleApproximateMatches: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = showExactMatches,
                onCheckedChange = onToggleExactMatches
            )
            Text("Exact Matches")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = showApproximateMatches,
                onCheckedChange = onToggleApproximateMatches
            )
            Text("Approximate Matches")
        }
    }
}

@Composable
fun SearchContent(
    searchState: SearchState,
    onCardClick: (Int) -> Unit,
    imageLoader: ImageLoader,
    combinedResults: List<CardGalleryEntry>
) {
    when (searchState) {
        is SearchState.Idle -> NoDataStatusScreen(
            emptyStateDescription = "",
            toastMessage = null
        )
        is SearchState.Loading -> LoadingStatusScreen()
        is SearchState.Empty -> NoDataStatusScreen(
            emptyStateDescription = "No cards found for provided\nquery and selection",
            toastMessage = null
        )
        is SearchState.Success -> SuccessStatusScreen {
            SearchResultsList(
                cards = combinedResults,
                onCardClick = onCardClick,
                imageLoader = imageLoader
            )
        }
        is SearchState.Error -> ErrorStatusScreen(
            "Error: ${searchState.message}",
            onRefreshClick = { },
        )
    }
}

@Composable
private fun SearchResultsList(
    cards: List<CardGalleryEntry>,
    onCardClick: (Int) -> Unit,
    imageLoader: ImageLoader
) {
    //todo rework this to not have this button
    SuccessStatusScreen(
        content = {
            CardList(
                cards = cards,
                onCardClick = onCardClick,
                onToggleLike = { _, _ ->

                },
                imageLoader = imageLoader
            )
        }
    )
}