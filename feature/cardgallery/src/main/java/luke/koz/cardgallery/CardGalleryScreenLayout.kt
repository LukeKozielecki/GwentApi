package luke.koz.cardgallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import luke.koz.cardgallery.components.CardGalleryStateHandler
import luke.koz.presentation.scaffold.CoreTopAppBar
import luke.koz.presentation.state.CardGalleryState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CardGalleryScreenLayout(
    cardState: CardGalleryState,
    onCardClick: (Int) -> Unit,
    onProfileClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onToggleLike: (Int, Boolean) -> Unit,
    onPullToRefresh: () -> Unit,
    onErrorRefreshRequest: () -> Unit,
    isRefreshing: Boolean,
    pullToRefreshState: PullToRefreshState,
    imageLoader: ImageLoader
) {
    Scaffold (
        topBar = {
            CoreTopAppBar(
                actions = {
                    IconButton(onClick = onSearchClicked) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                    IconButton(onClick = onProfileClicked) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "User Profile"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            state = pullToRefreshState,
            onRefresh = { onPullToRefresh() },
            isRefreshing = isRefreshing,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CardGalleryStateHandler(
                    state = cardState,
                    onCardClick = onCardClick,
                    onToggleLike = onToggleLike,
                    onRefreshClick = onErrorRefreshRequest,
                    imageLoader = imageLoader
                )
            }
        }
    }
}