package luke.koz.cardartshowcase

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.ImageLoader

/**
 * Public entry point composable for the Card Art Showcase feature.
 *
 * This composable acts as the root screen for displaying a specific card's art
 * with interactive zoom and pan functionalities.
 *
 * @param cardArtId The ID of the card art to be displayed.
 * @param cardColor The color string for the card border. Those are defined with strings in API.
 * @param imageLoader The Coil [ImageLoader] instance used for fetching images.
 */
@Composable
fun ArtShowcaseScreen(
    cardArtId: Int,
    cardColor: String,
    imageLoader: ImageLoader,
) {

    Scaffold { innerPadding ->
        ArtShowcaseScreenLayout(
            cardArtId = cardArtId,
            cardColor = cardColor,
            imageLoader = imageLoader,
            modifier = Modifier.padding(innerPadding)
        )
    }
}