package luke.koz.gwentapi.ui.cardgalleryscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import luke.koz.gwentapi.domain.viewModel.CardViewModel
import luke.koz.gwentapi.ui.cardgalleryscreen.components.HandleCardState
import luke.koz.gwentapi.ui.cardgalleryscreen.di.provideCardGalleryViewModel

@Composable
fun CardGalleryScreen(cardId: Int) {
    val viewModel: CardViewModel = provideCardGalleryViewModel()
    val cardState by viewModel.cardState

    LaunchedEffect(key1 = cardId) {
        viewModel.getAllCards()
    }

    HandleCardState(cardState)
}
