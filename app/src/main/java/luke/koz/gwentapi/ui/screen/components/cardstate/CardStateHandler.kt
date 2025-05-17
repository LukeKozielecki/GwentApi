package luke.koz.gwentapi.ui.screen.components.cardstate

import androidx.compose.runtime.Composable
import luke.koz.gwentapi.domain.model.CardDetailsEntry
import luke.koz.gwentapi.domain.model.CardGalleryEntry
import luke.koz.gwentapi.ui.state.CardDetailsState
import luke.koz.gwentapi.ui.state.CardState

@Composable
fun CardStateHandler(
    state: CardState,
    onEmpty: @Composable () -> Unit,
    onLoading: @Composable () -> Unit,
    onSuccess: @Composable (List<CardGalleryEntry>) -> Unit,
    onError: @Composable (String) -> Unit = { ErrorMessage(message = it) }
) {
    when (state) {
        is CardState.Empty -> onEmpty()
        is CardState.Loading -> onLoading()
        is CardState.Success -> onSuccess(state.cards)
        is CardState.Error -> onError(state.message)
    }
}

@Composable
fun CardDetailsStateHandlerOuter(
    state: CardDetailsState,
    onEmpty: @Composable () -> Unit,
    onLoading: @Composable () -> Unit,
    onSuccess: @Composable (List<CardDetailsEntry>) -> Unit,
    onError: @Composable (String) -> Unit = { ErrorMessage(message = it) }
) {
    when (state) {
        is CardDetailsState.Empty -> onEmpty()
        is CardDetailsState.Loading -> onLoading()
        is CardDetailsState.Success -> onSuccess(state.cards)
        is CardDetailsState.Error -> onError(state.message)
    }
}