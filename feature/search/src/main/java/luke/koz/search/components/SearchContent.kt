package luke.koz.search.components

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.presentation.state.SearchState
import luke.koz.presentation.statusscreen.ErrorStatusScreen
import luke.koz.presentation.statusscreen.LoadingStatusScreen
import luke.koz.presentation.statusscreen.NoDataStatusScreen
import luke.koz.presentation.statusscreen.SuccessStatusScreen

@Composable
internal fun SearchContent(
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