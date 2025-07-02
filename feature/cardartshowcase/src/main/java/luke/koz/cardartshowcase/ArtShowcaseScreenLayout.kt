package luke.koz.cardartshowcase

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import coil3.ImageLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import luke.koz.cardartshowcase.components.SuccessScreen
import luke.koz.cardartshowcase.model.ContentLoadState
import luke.koz.presentation.statusscreen.ErrorStatusScreen
import luke.koz.presentation.statusscreen.LoadingStatusScreen

private const val MAX_ZOOM_MULTIPLIER: Float = 4f

/**
 * Layout composable for displaying a card art showcase with interactive zoom and pan.
 *
 * This composable manages the overall UI state for image loading and gestures,
 * delegating the display of the interactive image to [SuccessScreen] once loaded.
 *
 * @param cardArtId The ID of the card art to be displayed.
 * @param cardColor The color string for the card border. Those are defined with strings in API.
 * @param imageLoader The Coil [ImageLoader] instance used for fetching images.
 * @param modifier The modifier to be applied to the [SuccessScreen] layout.
 */
@Composable
internal fun ArtShowcaseScreenLayout(
    cardArtId: Int,
    cardColor: String,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
) {
    val containerSize = remember { mutableStateOf(IntSize.Zero) }
    val currentScaleAnimated = remember { Animatable(1f) }
    val currentOffsetAnimated = remember { Animatable(Offset.Zero, Offset.VectorConverter) }

    val scope = rememberCoroutineScope()

    val transformableState = rememberTransformableState { zoomChange, offsetChange, _ ->
        handleTransformGesture(
            zoomChange = zoomChange,
            offsetChange = offsetChange,
            currentScaleAnimated = currentScaleAnimated,
            currentOffsetAnimated = currentOffsetAnimated,
            containerSize = containerSize.value,
            scope = scope,
            maxZoomMultiplier = MAX_ZOOM_MULTIPLIER
        )
    }

    var contentLoadState by remember { mutableStateOf(ContentLoadState.LOADING) }
    var isSuccessContentVisible by remember { mutableStateOf(false) }

    SuccessScreen(
        containerSize = containerSize,
        currentScaleAnimated = currentScaleAnimated,
        currentOffsetAnimated = currentOffsetAnimated,
        scope = scope,
        transformableState = transformableState,
        cardArtId = cardArtId,
        cardColor = cardColor,
        imageLoader = imageLoader,
        onSuccessLoad = {
            isSuccessContentVisible = true
            contentLoadState = ContentLoadState.SUCCESS
        },
        modifier = modifier.then(
            if (isSuccessContentVisible) Modifier else Modifier.alpha(0f)
        )
    )

    when (contentLoadState) {
        ContentLoadState.LOADING -> {
            LoadingStatusScreen()
        }
        ContentLoadState.SUCCESS -> {
            /* No overlay needed, SuccessScreen is visible */
        }
        ContentLoadState.ERROR -> ErrorStatusScreen(
            message = "Something went wrong while fetching the image",
            onRefreshClick = {}
        )
    }
}

/**
 * Handles the logic for transform gestures zoom and pan. Calculating
 * the new scale and offset values and updating the animated state.
 *
 * The offset is clamped to prevent the content from panning too far outside
 * the container boundaries at the current zoom level.
 *
 * @param zoomChange The change in zoom scale from the gesture.
 * @param offsetChange The change in offset from the gesture.
 * @param currentScaleAnimated The [Animatable] representing the current animated scale.
 * @param currentOffsetAnimated The [Animatable] representing the current animated offset.
 * @param containerSize The [IntSize] of the container holding the transformable content.
 * @param scope The [CoroutineScope] used to launch suspend function snapTo [Animatable.snapTo].
 * @param maxZoomMultiplier The maximum allowed zoom level.
 */
private fun handleTransformGesture(
    zoomChange: Float,
    offsetChange: Offset,
    currentScaleAnimated: Animatable<Float, *>,
    currentOffsetAnimated: Animatable<Offset, *>,
    containerSize: IntSize,
    scope: CoroutineScope,
    maxZoomMultiplier: Float
) {
    val newScale = (currentScaleAnimated.value * zoomChange)
        .coerceIn(1f, maxZoomMultiplier)

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