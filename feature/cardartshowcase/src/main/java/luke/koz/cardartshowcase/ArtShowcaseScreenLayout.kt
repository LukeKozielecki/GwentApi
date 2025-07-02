package luke.koz.cardartshowcase

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import luke.koz.presentation.card.CardImageWithBorder
import luke.koz.presentation.model.CardImageQuality

@Composable
internal fun ArtScreenShowcaseLayout(
    cardArtId: Int,
    cardColor: String,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        CardImageWithBorder(
            cardId = cardArtId,
            cardColor = cardColor,
            imageLoader = imageLoader,
            shouldFillMaxSize = true,
            cardImageQuality = CardImageQuality.HIGH
        )
    }
}