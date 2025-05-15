package luke.koz.gwentapi.ui.screen.carddetails

import androidx.compose.runtime.Composable
import luke.koz.gwentapi.ui.screen.components.cardstate.CardStateHandler
import luke.koz.gwentapi.ui.state.CardState

@Composable
fun CardDetailsStateHandler(state: CardState, onCardClick: (Int) -> Unit) {
    CardStateHandler(
        state = state,
        onEmpty = { /* Empty composable */ },
        onLoading = { /* Empty composable */ },
        onSuccess = { cards -> CardItemDetails(card = cards.first(), onCardClick) }
    )
}
