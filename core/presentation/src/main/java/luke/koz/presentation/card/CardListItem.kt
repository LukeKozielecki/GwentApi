package luke.koz.presentation.card

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    imageLoader: ImageLoader
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
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            IconButton(
                onClick = { onToggleLike(card.id, card.isLiked) },
                modifier = Modifier.border(
                    width = 2.dp,
                    color = if (card.isLiked) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondary
                    },
                    shape = MaterialTheme.shapes.small
                )
            ) {
                Row{
                    Icon(
                        imageVector = if (card.isLiked) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = "Like button",
                        tint = if (card.isLiked) { MaterialTheme.colorScheme.primary } else { MaterialTheme.colorScheme.secondary }
                    )
                    Spacer(Modifier.size(2.dp))
                    Text(card.likeCount.toString())
                }
            }
        }
    }
}