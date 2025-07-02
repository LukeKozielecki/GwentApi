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