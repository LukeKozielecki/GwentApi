package luke.koz.gwentapi.ui.screen.carddetails

import androidx.compose.runtime.Composable
import luke.koz.gwentapi.ui.screen.components.cardstate.CardDetailsStateHandlerOuter
import luke.koz.gwentapi.ui.screen.components.cardstate.CardStateHandler
import luke.koz.gwentapi.ui.state.CardDetailsState

@Composable
fun CardDetailsStateHandler(state: CardDetailsState, onCardClick: (Int) -> Unit) {
    CardDetailsStateHandlerOuter(
        state = state,
        onEmpty = { /* Empty composable */ },
        onLoading = { /* Empty composable */ },
        onSuccess = { cards -> CardItemDetails(card = cards.first(), onCardClick) }
    )
}
