package luke.koz.gwentapi.ui.screen.cardgallery

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import luke.koz.gwentapi.domain.model.CardGalleryEntry
import luke.koz.gwentapi.ui.screen.components.cardlist.CardList
import luke.koz.gwentapi.ui.screen.components.cardstate.EmptyState
import luke.koz.gwentapi.ui.screen.components.cardstate.ErrorMessage
import luke.koz.gwentapi.ui.state.CardState

@Composable
fun CardGalleryStateHandler(state: CardState, onCardClick : (Int) -> Unit) {
    CardGalleryStateHandlerInternal(
        state = state,
        onEmpty = { EmptyState() },
        onLoading = { CircularProgressIndicator() },
        onSuccess = { cards -> CardList(cards = cards, onCardClick) }
    )
}

@Composable
private fun CardGalleryStateHandlerInternal(
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