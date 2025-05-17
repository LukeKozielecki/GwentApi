package luke.koz.gwentapi.ui.screen.carddetails.components

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import luke.koz.gwentapi.application.GwentApplication
import luke.koz.gwentapi.domain.model.CardDetailsEntry
import luke.koz.gwentapi.ui.model.carddetails.CardImageOverlayModel
import luke.koz.gwentapi.ui.theme.Dimens

/**
 * Renders overlays (banners, power, provision, rarity) stacked on top of the card image.
 *
 * @param modifier The modifier applied to the root [Box]. **All overlays inherit this modifier
 * intentionally**, as they are designed to be stacked in specific positions based on the way
 * images are stored. Overlap is expected.
 */
@Composable
fun CardImageOverlay(
    card: CardDetailsEntry?,
    modifier: Modifier = Modifier.size(Dimens.CardSmall.Width, Dimens.CardSmall.Height),
) {
    val context = LocalContext.current
    val imageLoader = (context.applicationContext as GwentApplication).persistentImageLoader
    Box(modifier = modifier) {
        // Banner Top Overlay
        CardImageOverlayElement(
            imageModel = CardImageOverlayModel.BannerTopImageOverlay.Icon,
            context = context,
            imageLoader = imageLoader,
            modifier = modifier
        )

        // Banner Bottom Overlay
        CardImageOverlayElement(
            imageModel = CardImageOverlayModel.BannerBottomImageOverlay.Icon,
            context = context,
            imageLoader = imageLoader,
            modifier = modifier
        )

        // Power Overlay
        card?.power?.let { power ->
            CardImageOverlayElement(
                imageModel = CardImageOverlayModel.PowerImageOverlay.WithValue(power.toString()),
                context = context,
                imageLoader = imageLoader,
                modifier = modifier
            )
        }

        // Provision Icon
        CardImageOverlayElement(
            imageModel = CardImageOverlayModel.ProvisionImageOverlay.Icon,
            context = context,
            imageLoader = imageLoader,
            modifier = modifier
        )

        // Provision Number
        card?.provision?.let { provision ->
            CardImageOverlayElement(
                imageModel = CardImageOverlayModel.ProvisionImageOverlay.Number(provision.toString()),
                context = context,
                imageLoader = imageLoader,
                modifier = modifier
            )
        }

        // Rarity
        card?.rarity?.let { rarity ->
            CardImageOverlayElement(
                imageModel = CardImageOverlayModel.RarityImageOverlay.WithValue(rarity.lowercase()),
                context = context,
                imageLoader = imageLoader,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun CardImageOverlayElement(
    imageModel: CardImageOverlayModel,
    context: Context,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageModel.buildImageUrl())
                .diskCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = null,
            imageLoader = imageLoader,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
    }
}
