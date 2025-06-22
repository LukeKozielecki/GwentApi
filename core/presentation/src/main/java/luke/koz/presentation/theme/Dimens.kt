package luke.koz.presentation.theme

import androidx.compose.ui.unit.dp

/**
 * Centralizes dimension constants used across the app.
 *
 * Intent: Implemented as a single-file utility to limit clutter. If the number of dimensions grows
 * significantly, it is expected to be split into a `dimens` package with categorized files
 * (`CardDimens.kt`, `SpacingDimens.kt` etc.).
 */
object Dimens {
    object CardSmall {
        val Width = 150.dp
        val Height = 215.dp
    }
}