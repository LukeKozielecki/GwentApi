package luke.koz.cardgallery.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import coil3.ImageLoader
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.presentation.CardList
import luke.koz.presentation.CardLoadingScreen
import luke.koz.presentation.CardState
import luke.koz.presentation.EmptyState
import luke.koz.presentation.ErrorMessage

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
        onEmpty = { EmptyState() },
        onLoading = { CardLoadingScreen() },
        onSuccess = { cards ->
            CardList(
                cards = cards,
                onCardClick = onCardClick,
                onToggleLike = onToggleLike,
                imageLoader = imageLoader
            )
        },
        onError = {
            ErrorMessage(
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
    onError: @Composable (String) -> Unit = { ErrorMessage(message = it) {} }
) {
    when (state) {
        is CardState.Empty -> onEmpty()
        is CardState.Loading -> onLoading()
        is CardState.Success -> onSuccess(state.cards)
        is CardState.Error -> onError(state.message)
    }
}