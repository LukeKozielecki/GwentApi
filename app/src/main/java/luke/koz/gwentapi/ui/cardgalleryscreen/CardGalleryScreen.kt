package luke.koz.gwentapi.ui.cardgalleryscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import luke.koz.gwentapi.domain.state.CardState
import luke.koz.gwentapi.domain.viewModel.CardGalleryViewModel
import luke.koz.gwentapi.domain.viewModel.SearchViewModel
import luke.koz.gwentapi.ui.cardgalleryscreen.components.HandleCardState
import luke.koz.gwentapi.ui.cardgalleryscreen.di.provideCardGalleryViewModel
import luke.koz.gwentapi.ui.cardgalleryscreen.di.provideSearchGalleryViewModel

@Composable
fun CardGalleryScreen(cardId: Int, onCardClick : (Int) -> Unit) {
    val viewModel: CardGalleryViewModel = provideCardGalleryViewModel()
    val searchViewModel: SearchViewModel = provideSearchGalleryViewModel()
    val searchState by searchViewModel.searchState
    val cardState by viewModel.cardState

    //todo add pull from top to trigger .getAllCards() Pull to refresh https://developer.android.com/develop/ui/compose/components/pull-to-refresh
    LaunchedEffect(key1 = cardId) {
        if (cardState == CardState.Empty) {
            viewModel.getAllCards()
        }
    }
    Column {
//        SearchScreen(viewModel, searchState)
        HandleCardState(cardState, onCardClick)
    }
}
