package luke.koz.carddetails.components

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import luke.koz.domain.model.CardDetailsEntry
import luke.koz.presentation.CardDetailsState
import luke.koz.presentation.ErrorMessage

@Composable
fun CardDetailsStateHandler(
    state: CardDetailsState,
    onCardClick: (Int) -> Unit,
    imageLoader: ImageLoader
) {
    CardDetailsStateHandlerInternal(
        state = state,
        onEmpty = { /* Empty composable */ },
        onLoading = { /* Empty composable */ },
        onSuccess = { cards -> CardItemDetails(card = cards.first(), onCardClick, imageLoader) }
    )
}

@Composable
private fun CardDetailsStateHandlerInternal(
    state: CardDetailsState,
    onEmpty: @Composable () -> Unit,
    onLoading: @Composable () -> Unit,
    onSuccess: @Composable (List<CardDetailsEntry>) -> Unit,
    onError: @Composable (String) -> Unit = { ErrorMessage(message = it, onRefreshClick = {}) }
) {
    when (state) {
        is CardDetailsState.Empty -> onEmpty()
        is CardDetailsState.Loading -> onLoading()
        is CardDetailsState.Success -> onSuccess(state.cards)
        is CardDetailsState.Error -> onError(state.message)
    }
}