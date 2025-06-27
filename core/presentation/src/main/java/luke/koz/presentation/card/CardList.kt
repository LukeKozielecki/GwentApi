package luke.koz.presentation.card

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import coil3.ImageLoader
import luke.koz.domain.model.CardGalleryEntry

@Composable
fun CardList(
    cards: List<CardGalleryEntry>,
    onCardClick: (Int) -> Unit,
    onToggleLike: (Int, Boolean) -> Unit,
    imageLoader: ImageLoader
) {
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
}