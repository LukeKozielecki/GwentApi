package luke.koz.carddetails.components

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import luke.koz.domain.model.CardDetailsEntry
import luke.koz.presentation.state.CardDetailsState
import luke.koz.presentation.statusscreen.LoadingStatusScreen
import luke.koz.presentation.statusscreen.ErrorStatusScreen

@Composable
fun CardDetailsStateHandler(
    state: CardDetailsState,
    onCardClick: (Int, String) -> Unit,
    imageLoader: ImageLoader
) {
    CardDetailsStateHandlerInternal(
        state = state,
        onEmpty = { /* Empty composable */ },
        onLoading = { LoadingStatusScreen() },
        onSuccess = { cards -> CardItemDetails(card = cards.first(), onCardClick, imageLoader) }
    )
}

@Composable
private fun CardDetailsStateHandlerInternal(
    state: CardDetailsState,
    onEmpty: @Composable () -> Unit,
    onLoading: @Composable () -> Unit,
    onSuccess: @Composable (List<CardDetailsEntry>) -> Unit,
    onError: @Composable (String) -> Unit = { ErrorStatusScreen(message = it, onRefreshClick = {}) }
) {
    when (state) {
        is CardDetailsState.Empty -> onEmpty()
        is CardDetailsState.Loading -> onLoading()
        is CardDetailsState.Success -> onSuccess(state.cards)
        is CardDetailsState.Error -> onError(state.message)
    }
}