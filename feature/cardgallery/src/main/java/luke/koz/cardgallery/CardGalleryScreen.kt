package luke.koz.cardgallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import coil3.ImageLoader
import luke.koz.cardgallery.viewmodel.CardGalleryViewModel
import luke.koz.cardgallery.viewmodel.provideCardGalleryViewModel

@Composable
fun CardGalleryScreen(
    onCardClick: (Int) -> Unit,
    onProfileClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    imageLoader: ImageLoader
) {
    val viewModel: CardGalleryViewModel = provideCardGalleryViewModel()
    val cardState by viewModel.cardGalleryState

    CardGalleryScreenLayout(
        cardState = cardState,
        onCardClick = onCardClick,
        onProfileClicked = onProfileClicked,
        onSearchClicked = onSearchClicked,
        onToggleLike = viewModel::toggleLike,
        onRefreshClick = viewModel::getAllCards,
        imageLoader = imageLoader
    )
}