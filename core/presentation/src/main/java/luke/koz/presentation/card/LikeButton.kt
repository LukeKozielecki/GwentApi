package luke.koz.presentation.card

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Custom like button with a count.
 *
 * @param isLiked Signifier if the item is currently liked.
 * @param likeCount Total number of likes.
 * @param onToggleLike Callback to be invoked when the button is clicked to toggle like status.
 * @param modifier Optional [Modifier] for this composable.
 */
@Composable
internal fun LikeButton(
    isLiked: Boolean,
    likeCount: Int,
    onToggleLike: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onToggleLike,
        modifier = Modifier.border(
            width = 2.dp,
            color = if (isLiked) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.secondary
            },
            shape = MaterialTheme.shapes.small
        )
    ) {
        Row {
            Icon(
                imageVector = if (isLiked) {
                    Icons.Default.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = if (isLiked) "Unlike button" else "Like button",
                tint = if (isLiked) { MaterialTheme.colorScheme.primary } else { MaterialTheme.colorScheme.secondary }
            )
            Spacer(Modifier.size(2.dp))
            Text(likeCount.toString())
        }
    }
}