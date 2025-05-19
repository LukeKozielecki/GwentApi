package luke.koz.gwentapi.ui.screen.cardgallery

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import luke.koz.gwentapi.di.provideCardGalleryViewModel
import luke.koz.gwentapi.di.provideSearchGalleryViewModel
import luke.koz.gwentapi.ui.scaffold.DefaultScaffold
import luke.koz.gwentapi.ui.scaffold.components.ScaffoldWrapper
import luke.koz.gwentapi.ui.state.CardState
import luke.koz.gwentapi.ui.viewmodel.CardGalleryViewModel
import luke.koz.gwentapi.ui.viewmodel.SearchViewModel

@Composable
fun CardGalleryScreen(
    cardId: Int,
    onCardClick: (Int) -> Unit,
    navController: NavHostController,
    scaffold: @Composable (NavHostController, @Composable (PaddingValues) -> Unit) -> Unit = { nav, content ->
        DefaultScaffold(navController = nav, content = content)
    }
) {
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

    ScaffoldWrapper(
        navController = navController,
        scaffold = scaffold
    ) {
        CardGalleryStateHandler(cardState, onCardClick)
    }
}
