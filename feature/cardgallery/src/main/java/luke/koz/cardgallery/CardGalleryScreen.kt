package luke.koz.cardgallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import luke.koz.cardgallery.components.CardGalleryStateHandler
import luke.koz.cardgallery.viewmodel.CardGalleryViewModel
import luke.koz.cardgallery.viewmodel.provideCardGalleryViewModel
import luke.koz.presentation.scaffold.GwentTopAppBar

@Composable
fun CardGalleryScreen(
    onCardClick: (Int) -> Unit,
    onProfileClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    imageLoader: ImageLoader
) {
    val viewModel: CardGalleryViewModel = provideCardGalleryViewModel()
    val cardState by viewModel.cardGalleryState

    //todo add pull from top to trigger .getAllCards() Pull to refresh
    // https://developer.android.com/develop/ui/compose/components/pull-to-refresh

    Scaffold (
        topBar = {
            GwentTopAppBar(
                onProfileClicked = onProfileClicked,
                onSearchClicked = onSearchClicked
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
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