package luke.koz.gwentapi.ui.screen.carddetails

import androidx.compose.runtime.Composable
import luke.koz.gwentapi.domain.model.CardDetailsEntry
import luke.koz.gwentapi.ui.screen.components.cardstate.ErrorMessage
import luke.koz.gwentapi.ui.state.CardDetailsState

@Composable
fun CardDetailsStateHandler(state: CardDetailsState, onCardClick: (Int) -> Unit) {
    CardDetailsStateHandlerInternal(
        state = state,
        onEmpty = { /* Empty composable */ },
        onLoading = { /* Empty composable */ },
        onSuccess = { cards -> CardItemDetails(card = cards.first(), onCardClick) }
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