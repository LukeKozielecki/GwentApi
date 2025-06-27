package luke.koz.cardgallery.components

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.presentation.CardListNew
import luke.koz.presentation.SuccessStatusScreen
import luke.koz.presentation.LoadingStatusScreen
import luke.koz.presentation.CardState
import luke.koz.presentation.NoDataStatusScreen
import luke.koz.presentation.ErrorStatusScreen

@Composable
fun CardGalleryStateHandler(
    state: CardState,
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
                    CardListNew(
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
    state: CardState,
    onEmpty: @Composable () -> Unit,
    onLoading: @Composable () -> Unit,
    onSuccess: @Composable (List<CardGalleryEntry>) -> Unit,
    onError: @Composable (String) -> Unit = { ErrorStatusScreen(message = it) {} }
) {
    when (state) {
        is CardState.Empty -> onEmpty()
        is CardState.Loading -> onLoading()
        is CardState.Success -> onSuccess(state.cards)
        is CardState.Error -> onError(state.message)
    }
}