package luke.koz.cardgallery

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coil3.ImageLoader
import luke.koz.cardgallery.viewmodel.CardGalleryViewModel
import luke.koz.cardgallery.viewmodel.provideCardGalleryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardGalleryScreen(
    onCardClick: (Int) -> Unit,
    onProfileClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    imageLoader: ImageLoader
) {
    val viewModel: CardGalleryViewModel = provideCardGalleryViewModel()
    val cardState by viewModel.cardGalleryState
    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    CardGalleryScreenLayout(
        cardState = cardState,
        onCardClick = onCardClick,
        onProfileClicked = onProfileClicked,
        onSearchClicked = onSearchClicked,
        onToggleLike = viewModel::toggleLike,
        onPullToRefresh = viewModel::onPullToRefresh,
        onErrorRefreshRequest = viewModel::getAllCards,
        pullToRefreshState = pullToRefreshState,
        isRefreshing = isRefreshing,
        imageLoader = imageLoader
    )
}