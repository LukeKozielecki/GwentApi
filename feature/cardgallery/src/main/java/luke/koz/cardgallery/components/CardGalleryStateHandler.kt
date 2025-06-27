package luke.koz.cardgallery.components

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.presentation.card.CardList
import luke.koz.presentation.statusscreen.SuccessStatusScreen
import luke.koz.presentation.statusscreen.LoadingStatusScreen
import luke.koz.presentation.state.CardGalleryState
import luke.koz.presentation.statusscreen.NoDataStatusScreen
import luke.koz.presentation.statusscreen.ErrorStatusScreen

@Composable
fun CardGalleryStateHandler(
    state: CardGalleryState,
    onCardClick: (Int) -> Unit,
    onToggleLike: (Int, Boolean) -> Unit,
    onRefreshClick : () -> Unit,
    imageLoader: ImageLoader
) {
    CardGalleryStateHandlerInternal(
        state = state,
        onEmpty = { NoDataStatusScreen(
            emptyStateDescription = "No cards found",
            toastMessage = "Please try checking internet connection"
        ) },
        onLoading = { LoadingStatusScreen() },
        onSuccess = { cards ->
            SuccessStatusScreen(
                content = {
                    CardList(
                        cards = cards,
                        onCardClick = onCardClick,
                        onToggleLike = onToggleLike,
                        imageLoader = imageLoader
                    )
                }
            )
        },
        onError = {
            ErrorStatusScreen(
                message = it,
                onRefreshClick = onRefreshClick
            )
        }
    )
}

@Composable
private fun CardGalleryStateHandlerInternal(
    state: CardGalleryState,
    onEmpty: @Composable () -> Unit,
    onLoading: @Composable () -> Unit,
    onSuccess: @Composable (List<CardGalleryEntry>) -> Unit,
    onError: @Composable (String) -> Unit = { ErrorStatusScreen(message = it) {} }
) {
    when (state) {
        is CardGalleryState.Empty -> onEmpty()
        is CardGalleryState.Loading -> onLoading()
        is CardGalleryState.Success -> onSuccess(state.cards)
        is CardGalleryState.Error -> onError(state.message)
    }
}