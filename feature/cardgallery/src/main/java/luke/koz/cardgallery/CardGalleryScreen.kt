package luke.koz.cardgallery

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import coil3.ImageLoader
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import luke.koz.cardgallery.viewmodel.CardGalleryViewModel
import luke.koz.presentation.scaffold.DefaultScaffold
import luke.koz.presentation.scaffold.components.ScaffoldWrapper
import luke.koz.search.SearchViewModel
import luke.koz.search.provideSearchGalleryViewModel

@Composable
fun CardGalleryScreen(
    onCardClick: (Int) -> Unit,
    navController: NavHostController,
    scaffold: @Composable (NavHostController, @Composable (PaddingValues) -> Unit) -> Unit = { nav, content ->
        DefaultScaffold(navController = nav, content = content)
    },
    imageLoader: ImageLoader
) {
    val viewModel: CardGalleryViewModel = provideCardGalleryViewModel()
    val searchViewModel: SearchViewModel = provideSearchGalleryViewModel()
    val searchState by searchViewModel.searchState
    val cardState by viewModel.cardState

    //todo add pull from top to trigger .getAllCards() Pull to refresh https://developer.android.com/develop/ui/compose/components/pull-to-refresh
//    LaunchedEffect() {
//        if (cardState == CardState.Empty) {
//            viewModel.getAllCards()
//        }
//    }

    ScaffoldWrapper(
        navController = navController,
        scaffold = scaffold
    ) {
        CardGalleryStateHandler(
            state = cardState,
            onCardClick = onCardClick,
            onToggleLike = viewModel::toggleLike,
            onRefreshClick = viewModel::getAllCards,
            imageLoader = imageLoader
        )
    }
}

@Preview
@Composable
private fun CardGalleryScreenPrev() {

}