package luke.koz.gwentapi.ui.cardgalleryscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import luke.koz.gwentapi.domain.viewModel.CardViewModel
import luke.koz.gwentapi.domain.viewModel.SearchState
import luke.koz.gwentapi.ui.cardgalleryscreen.components.HandleCardState
import luke.koz.gwentapi.ui.cardgalleryscreen.components.SearchScreen
import luke.koz.gwentapi.ui.cardgalleryscreen.di.provideCardGalleryViewModel

@Composable
fun CardGalleryScreen(cardId: Int) {
    val viewModel: CardViewModel = provideCardGalleryViewModel()
    val searchState by viewModel.searchState
    val cardState by viewModel.cardState

    LaunchedEffect(key1 = cardId) {
        viewModel.getAllCards()
    }
    Column {
//        SearchScreen(viewModel, searchState)
        HandleCardState(cardState)
    }
}
