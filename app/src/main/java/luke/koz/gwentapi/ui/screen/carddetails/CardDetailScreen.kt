package luke.koz.gwentapi.ui.screen.carddetails

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import luke.koz.gwentapi.ui.state.CardState
import luke.koz.gwentapi.ui.viewmodel.CardDetailViewModel
import luke.koz.gwentapi.ui.screen.sharedcomponents.HandleDetailsCardState
import luke.koz.gwentapi.di.provideCardDetailViewModel

@Composable
fun CardDetailScreen(
    cardId : Int,
    onBack : () -> Unit
) {
    val viewModel: CardDetailViewModel = provideCardDetailViewModel()
    val cardState by viewModel.cardState

    LaunchedEffect(key1 = cardId) {
        if (cardState == CardState.Empty) {
            viewModel.getCardById(cardId)
        }
    }

    Column {
        HandleDetailsCardState(
            state = cardState,
            onCardClick = {  }
        )
    }
}