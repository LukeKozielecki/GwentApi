package luke.koz.cardgallery.components

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import luke.koz.presentation.card.CardList
import luke.koz.presentation.state.CardGalleryState
import luke.koz.presentation.statusscreen.ErrorStatusScreen
import luke.koz.presentation.statusscreen.LoadingStatusScreen
import luke.koz.presentation.statusscreen.NoDataStatusScreen
import luke.koz.presentation.statusscreen.SuccessStatusScreen

@Composable
internal fun CardGalleryStateHandler(
    state: CardGalleryState,
    onCardClick: (Int) -> Unit,
    onToggleLike: (Int, Boolean) -> Unit,
    onRefreshClick : () -> Unit,
    imageLoader: ImageLoader
) {
    when (state) {
        is CardGalleryState.Empty -> NoDataStatusScreen(
            emptyStateDescription = "No cards found",
            toastMessage = "Please try checking internet connection"
        )
        is CardGalleryState.Loading -> LoadingStatusScreen()
        is CardGalleryState.Success -> SuccessStatusScreen(
            content = {
                CardList(
                    cards = state.cards,
                    onCardClick = onCardClick,
                    onToggleLike = onToggleLike,
                    imageLoader = imageLoader
                )
            }
        )
        is CardGalleryState.Error -> ErrorStatusScreen(
            message = state.message,
            onRefreshClick = onRefreshClick
        )
    }
}