package luke.koz.auth.model

/**
 * Represents the state of an individual input field within an authentication form.
 * This data class is used to track user interaction with the field.
 *
 * @property value The current text content of the input field. Defaults to empty.
 * @property isTouched A boolean indicating if the user is interacting.
 * In tandem with [hasBeenFocused] it determines user communication. Defaults to false.
 * @property hasBeenFocused A boolean indicating if the field has ever been interacted with.
 * In tandem with [isTouched] it determines user communication. Defaults to false.
 */
data class AuthFieldState(
    val value: String = "",
    val isTouched: Boolean = false,
    val hasBeenFocused: Boolean = false
)