package luke.koz.cardartshowcase

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.ImageLoader

@Composable
fun ArtShowCaseScreen(
    cardArtId: Int,
    cardColor: String,
    imageLoader: ImageLoader,
) {

    Scaffold { innerPadding ->
        ArtScreenShowcaseLayout(
            cardArtId = cardArtId,
            cardColor = cardColor,
            imageLoader = imageLoader,
            modifier = Modifier.padding(innerPadding)
        )
    }
}