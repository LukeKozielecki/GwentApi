package luke.koz.presentation.statusscreen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import coil3.ImageLoader
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.presentation.CardItem


@Composable
fun SuccessStatusScreen(
    content: @Composable () -> Unit
) {
    content()
}

@Composable
fun CardListNew(
    cards: List<CardGalleryEntry>,
    onCardClick: (Int) -> Unit,
    onToggleLike: (Int, Boolean) -> Unit,
    imageLoader: ImageLoader
) {
//    if (cards.isEmpty()) { //this geniunely should never reach no cards state
//        NoDataStatusScreen(
//            emptyStateDescription = "No cards found",
//            toastMessage = "Please try checking internet connection"
//        )
//    } else {
    LazyColumn {
        items(cards) { card ->
            CardItem(
                card = card,
                onCardClick = onCardClick,
                onToggleLike = onToggleLike,
                imageLoader = imageLoader
            )
            HorizontalDivider()
        }
    }
//    }
}