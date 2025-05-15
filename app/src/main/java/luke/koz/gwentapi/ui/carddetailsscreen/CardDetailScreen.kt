package luke.koz.gwentapi.ui.carddetailsscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import luke.koz.gwentapi.domain.state.CardState
import luke.koz.gwentapi.domain.viewModel.CardDetailViewModel
import luke.koz.gwentapi.domain.viewModel.CardGalleryViewModel
import luke.koz.gwentapi.domain.viewModel.SearchViewModel
import luke.koz.gwentapi.ui.cardgalleryscreen.components.HandleCardState
import luke.koz.gwentapi.ui.cardgalleryscreen.components.HandleDetailsCardState
import luke.koz.gwentapi.ui.cardgalleryscreen.di.provideCardDetailViewModel
import luke.koz.gwentapi.ui.cardgalleryscreen.di.provideCardGalleryViewModel
import luke.koz.gwentapi.ui.cardgalleryscreen.di.provideSearchGalleryViewModel

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