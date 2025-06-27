package luke.koz.cardgallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import coil3.ImageLoader
import luke.koz.cardgallery.components.CardGalleryStateHandler
import luke.koz.cardgallery.viewmodel.CardGalleryViewModel
import luke.koz.cardgallery.viewmodel.provideCardGalleryViewModel
import luke.koz.presentation.scaffold.DefaultScaffold
import luke.koz.presentation.scaffold.components.ScaffoldWrapper

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
    val cardState by viewModel.cardState

    //todo add pull from top to trigger .getAllCards() Pull to refresh https://developer.android.com/develop/ui/compose/components/pull-to-refresh

    ScaffoldWrapper(
        navController = navController,
        scaffold = scaffold
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
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
}