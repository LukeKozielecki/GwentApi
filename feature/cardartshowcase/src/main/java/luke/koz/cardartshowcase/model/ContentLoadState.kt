package luke.koz.cardartshowcase.model

/**
 * Represents the possible states of content loading within the ArtShowcaseScreen UI.
 * It determines which UI should be rendered.
 */
internal enum class ContentLoadState {
    LOADING,
    SUCCESS,
    ERROR
}
