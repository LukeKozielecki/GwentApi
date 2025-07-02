package luke.koz.cardartshowcase.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import luke.koz.presentation.card.CardImageWithBorder
import luke.koz.presentation.model.CardImageQuality

/**
 * A Composable that displays the successfully loaded card art with interactive zoom and pan
 * functionalities.
 *
 * This component is invoked for happy path where the image content is ready to be displayed
 * and provides the UI for gestures and animations.
 * It receives all necessary state and callback handlers from its parent.
 *
 * @param containerSize A [MutableState] holding the [IntSize] of the container, allowing this
 * composable to update it when its size changes via [Modifier.onSizeChanged].
 * @param currentScaleAnimated An [Animatable] controlling the current zoom scale of the content.
 * @param currentOffsetAnimated An [Animatable] controlling the current pan offset of the content.
 * @param scope The [CoroutineScope] used for launching animation coroutines.
 * @param transformableState The [TransformableState] that handles and applies gesture input for
 * zoom and pan.
 * @param cardArtId The ID of the card art to be displayed.
 * @param cardColor The color string for the card border. Those are defined with strings in API.
 * @param imageLoader The Coil [ImageLoader] instance used by [CardImageWithBorder] to fetch images.
 * @param onSuccessLoad A callback invoked when the primary image inside [CardImageWithBorder]
 * successfully loads. This is used to signal the parent that the content is fully ready.
 * @param modifier Optional modifier to be applied to the root layout of this composable.
 */
@Composable
internal fun SuccessScreen(
    containerSize: MutableState<IntSize>,
    currentScaleAnimated: Animatable<Float, *>,
    currentOffsetAnimated: Animatable<Offset, *>,
    scope: CoroutineScope,
    transformableState: TransformableState,
    cardArtId: Int,
    cardColor: String,
    imageLoader: ImageLoader,
    onSuccessLoad: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { containerSize.value = it }
            .graphicsLayer(
                scaleX = currentScaleAnimated.value,
                scaleY = currentScaleAnimated.value,
                translationX = currentOffsetAnimated.value.x,
                translationY = currentOffsetAnimated.value.y
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        scope.launch {
                            launch {
                                currentScaleAnimated.animateTo(1f)
                            }
                            launch {
                                currentOffsetAnimated.animateTo(Offset.Zero)
                            }
                        }
                    }
                )
            }
            .transformable(state = transformableState),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            CardImageWithBorder(
                cardId = cardArtId,
                cardColor = cardColor,
                imageLoader = imageLoader,
                shouldFillMaxSize = true,
                cardImageQuality = CardImageQuality.HIGH,
                borderImageQuality = CardImageQuality.HIGH,
                onLoadSuccess = onSuccessLoad
            )
        }
    }
}