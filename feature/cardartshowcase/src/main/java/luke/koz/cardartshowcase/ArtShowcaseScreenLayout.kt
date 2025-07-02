package luke.koz.cardartshowcase

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import luke.koz.presentation.card.CardImageWithBorder

@Composable
internal fun ArtScreenShowcaseLayout(
    cardArtId: Int,
    cardColor: String,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CardImageWithBorder(
            cardArtId,
            cardColor,
            imageLoader
        )
    }
}