package luke.koz.cardartshowcase

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import kotlinx.coroutines.launch
import luke.koz.presentation.card.CardImageWithBorder
import luke.koz.presentation.model.CardImageQuality

private const val MAX_ZOOM_MULTIPLIER: Float = 4f

@Composable
internal fun ArtScreenShowcaseLayout(
    cardArtId: Int,
    cardColor: String,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
) {
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val currentScaleAnimated = remember { Animatable(1f) }
    val currentOffsetAnimated = remember { Animatable(Offset.Zero, Offset.VectorConverter) }

    val scope = rememberCoroutineScope()

    val transformableState = rememberTransformableState { zoomChange, offsetChange, _ ->
        val newScale = (currentScaleAnimated.value * zoomChange)
            .coerceIn(1f, MAX_ZOOM_MULTIPLIER)

        val maxTranslateX = (containerSize.width * newScale - containerSize.width) / 2f
        val maxTranslateY = (containerSize.height * newScale - containerSize.height) / 2f

        val currentOffsetX = currentOffsetAnimated.value.x + offsetChange.x * newScale
        val currentOffsetY = currentOffsetAnimated.value.y + offsetChange.y * newScale

        val calculatedCurrentOffset = Offset(
            x = currentOffsetX.coerceIn(-maxTranslateX, maxTranslateX),
            y = currentOffsetY.coerceIn(-maxTranslateY, maxTranslateY)
        )

        scope.launch {
            currentScaleAnimated.snapTo(newScale)
            currentOffsetAnimated.snapTo(calculatedCurrentOffset)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { containerSize = it }
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
                cardImageQuality = CardImageQuality.HIGH
            )
        }
    }
}