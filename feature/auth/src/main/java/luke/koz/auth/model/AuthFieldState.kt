package luke.koz.auth.model


data class AuthFieldState(
    val value: String = "",
    val isTouched: Boolean = false,
    val hasBeenFocused: Boolean = false
)