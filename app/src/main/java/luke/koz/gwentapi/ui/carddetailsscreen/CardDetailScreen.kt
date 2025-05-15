package luke.koz.gwentapi.ui.carddetailsscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import luke.koz.gwentapi.domain.state.CardState
import luke.koz.gwentapi.domain.viewModel.CardDetailViewModel
import luke.koz.gwentapi.ui.cardgalleryscreen.components.HandleDetailsCardState
import luke.koz.gwentapi.ui.cardgalleryscreen.di.provideCardDetailViewModel

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