package luke.koz.presentation.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import luke.koz.domain.model.CardGalleryEntry

@Composable
internal fun CardItem(
    card: CardGalleryEntry,
    onCardClick: (Int) -> Unit,
    onToggleLike: (Int, Boolean) -> Unit,
    imageLoader: ImageLoader,
    shouldShowLikeButton: Boolean = true
) {
    Box(
        Modifier
            .fillMaxWidth()
            .clickable { onCardClick(card.id) }
    ) {
        Row {
            CardImageWithBorder(card.artId, card.color, imageLoader)
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = card.name, style = MaterialTheme.typography.titleMedium)
                Text(text = card.flavor, style = MaterialTheme.typography.bodySmall)
            }
        }
        if (shouldShowLikeButton) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                LikeButton(
                    isLiked = card.isLiked,
                    likeCount = card.likeCount,
                    onToggleLike = { onToggleLike(card.id, card.isLiked) },
                )
            }
        }
    }
}