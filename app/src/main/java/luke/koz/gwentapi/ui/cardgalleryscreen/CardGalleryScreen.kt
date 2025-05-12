package luke.koz.gwentapi.ui.cardgalleryscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import luke.koz.gwentapi.domain.viewModel.CardGalleryViewModel
import luke.koz.gwentapi.domain.viewModel.SearchViewModel
import luke.koz.gwentapi.ui.cardgalleryscreen.components.HandleCardState
import luke.koz.gwentapi.ui.cardgalleryscreen.di.provideCardGalleryViewModel
import luke.koz.gwentapi.ui.cardgalleryscreen.di.provideSearchGalleryViewModel

@Composable
fun CardGalleryScreen(cardId: Int) {
    val viewModel: CardGalleryViewModel = provideCardGalleryViewModel()
    val searchViewModel: SearchViewModel = provideSearchGalleryViewModel()
    val searchState by searchViewModel.searchState
    val cardState by viewModel.cardState

    LaunchedEffect(key1 = cardId) {
        viewModel.getAllCards()
    }
    Column {
//        SearchScreen(viewModel, searchState)
        HandleCardState(cardState)
    }
}
