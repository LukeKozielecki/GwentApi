package luke.koz.gwentapi.ui.screen.cardgallery

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import luke.koz.gwentapi.ui.screen.components.cardlist.CardList
import luke.koz.gwentapi.ui.screen.components.cardstate.EmptyState
import luke.koz.gwentapi.ui.screen.components.cardstate.CardStateHandler
import luke.koz.gwentapi.ui.state.CardState

@Composable
fun CardGalleryStateHandler(state: CardState, onCardClick : (Int) -> Unit) {
    CardStateHandler(
        state = state,
        onEmpty = { EmptyState() },
        onLoading = { CircularProgressIndicator() },
        onSuccess = { cards -> CardList(cards = cards, onCardClick) }
    )
}