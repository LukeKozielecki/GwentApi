package luke.koz.gwentapi.ui.model.auth


data class AuthFieldState(
    val value: String = "",
    val isTouched: Boolean = false,
    val hasBeenFocused: Boolean = false
)