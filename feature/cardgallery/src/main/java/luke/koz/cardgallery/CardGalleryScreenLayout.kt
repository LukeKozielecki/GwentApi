package luke.koz.cardgallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import luke.koz.cardgallery.components.CardGalleryStateHandler
import luke.koz.presentation.scaffold.CoreTopAppBar
import luke.koz.presentation.state.CardGalleryState

@Composable
internal fun CardGalleryScreenLayout(
    cardState: CardGalleryState,
    onCardClick: (Int) -> Unit,
    onProfileClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onToggleLike: (Int, Boolean) -> Unit,
    onRefreshClick: () -> Unit,
    imageLoader: ImageLoader
) {
    //todo add pull from top to trigger .getAllCards() Pull to refresh
    // https://developer.android.com/develop/ui/compose/components/pull-to-refresh

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
                onToggleLike = onToggleLike,
                onRefreshClick = onRefreshClick,
                imageLoader = imageLoader
            )
        }

    }
}