package luke.koz.carddetails.components

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import luke.koz.carddetails.model.CardImageOverlayModel
import luke.koz.domain.model.CardAttributeType
import luke.koz.domain.model.CardDetailsEntry

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
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier.size(luke.koz.presentation.theme.Dimens.CardSmall.Width, luke.koz.presentation.theme.Dimens.CardSmall.Height),
) {
    val context = LocalContext.current
//    val imageLoader = (context.applicationContext as GwentApplication).persistentImageLoader
    val overlays = listOf(
        CardImageOverlayConfig(
            imageModel = CardImageOverlayModel.BannerTopImageOverlay.Icon,
        ),
        CardImageOverlayConfig(
            imageModel = CardImageOverlayModel.BannerBottomImageOverlay.Icon,
        ),
        CardImageOverlayConfig(
            imageModel = CardImageOverlayModel.ProvisionImageOverlay.Icon,
        ),
        CardImageOverlayConfig(
            imageModel = CardImageOverlayModel.ArmourImageOverlay.Icon,
            condition = card?.armor != 0
        ),
        CardImageOverlayConfig(
            imageModel = CardImageOverlayModel.TypeImageOverlay.IconSpecial,
            condition = card?.type == CardAttributeType.SPECIAL.type
        ),
        CardImageOverlayConfig(
            imageModel = CardImageOverlayModel.TypeImageOverlay.IconArtifact,
            condition = card?.type == CardAttributeType.ARTIFACT.type
        ),
        CardImageOverlayConfig(
            imageModel = card?.power?.let {
                CardImageOverlayModel.PowerImageOverlay.WithValue(it.toString())
            },
            condition = card?.power != 0
        ),
        CardImageOverlayConfig(
            imageModel = card?.provision?.let {
                CardImageOverlayModel.ProvisionImageOverlay.Number(it.toString())
            },
            condition = card?.provision != null
        ),
        CardImageOverlayConfig(
            imageModel = card?.provision?.let {
                CardImageOverlayModel.ArmourImageOverlay.Number(it.toString())
            },
            condition = card?.armor != 0
        ),
        CardImageOverlayConfig(
            imageModel = card?.rarity?.let {
                CardImageOverlayModel.RarityImageOverlay.WithValue(it.lowercase())
            },
            condition = card?.rarity != null
        )
    )
    Box(modifier = modifier) {
        overlays.forEach { overlay ->
            if (overlay.condition) {
                overlay.imageModel?.let { model ->
                    CardImageOverlayElement(
                        imageModel = model,
                        context = context,
                        imageLoader = imageLoader,
                        modifier = modifier
                    )
                }
            }
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

private data class CardImageOverlayConfig(
    val imageModel: CardImageOverlayModel?,
    val condition: Boolean = true
)