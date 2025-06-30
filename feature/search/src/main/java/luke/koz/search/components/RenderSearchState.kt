package luke.koz.search.components

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.presentation.state.SearchState
import luke.koz.presentation.statusscreen.ErrorStatusScreen
import luke.koz.presentation.statusscreen.LoadingStatusScreen
import luke.koz.presentation.statusscreen.NoDataStatusScreen
import luke.koz.presentation.statusscreen.SuccessStatusScreen

/**
 * Renders the appropriate UI based on [SearchState].
 *
 * This composable acts as a state-to-UI mapper for the search screen, displaying
 * different result (idle, loading, empty, success, or error) depending on the
 * received [searchState]. It delegates the actual content rendering for each
 * state to specialized status functions.
 *
 * @param searchState The current state of the search operation. Determines which UI to display.
 * @param onCardClick A callback function invoked when a card item in the success state is clicked.
 * @param imageLoader An [ImageLoader] instance used for loading images within the displayed content
 * for the card list in the success state.
 * @param combinedResults The list of [CardGalleryEntry] items to display when the [searchState]
 * is [SearchState.Success]. These results are expected to be already filtered.
 */
@Composable
internal fun RenderSearchState(
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
            FilteredCardList(
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