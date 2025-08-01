package luke.koz.presentation.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import luke.koz.presentation.R
import luke.koz.presentation.model.CardImageQuality
import luke.koz.presentation.theme.GwentApiTheme

@Composable
fun CardImageWithBorder(
    cardId: Int,
    cardColor: String,
    imageLoader: ImageLoader,
    shouldFillMaxSize : Boolean = false,
    cardImageQuality: CardImageQuality = CardImageQuality.LOW,
    borderImageQuality: CardImageQuality = CardImageQuality.MEDIUM,
    onLoadSuccess: (() -> Unit)? = null,
    onLoadError: (() -> Unit)? = null
) {
    val context = LocalContext.current
    Box(modifier = Modifier
        .then(
            if (shouldFillMaxSize) {
                Modifier
                    .fillMaxSize()
                    .aspectRatio(150f / 215f)
            } else {
                Modifier.size(150.dp, 215.dp)
            }
        )
    ) {
        Image(
            painter = painterResource(R.drawable.gwent_one_api_favicon_96x96),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("https://gwent.one/image/gwent/assets/card/art/${cardImageQuality.value}/${cardId}.jpg")
                .diskCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = null,
            imageLoader = imageLoader,
            contentScale = ContentScale.FillBounds,
            onSuccess = { onLoadSuccess?.invoke() },
            onError = { onLoadError?.invoke() },
            modifier = Modifier.matchParentSize()
        )
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("https://gwent.one/image/gwent/assets/card/other/${borderImageQuality.value}/border_${cardColor.lowercase()}.png")
                .diskCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = null,
            imageLoader = imageLoader,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
    }
}

@Preview
@Composable
private fun CardImageWithBorderPrev() {
    val previewImageLoader = ImageLoader.Builder(LocalContext.current).build()
    GwentApiTheme {
        CardImageWithBorder(
            cardId = 1636,
            cardColor = "gold",
            imageLoader = previewImageLoader
        )
    }
}