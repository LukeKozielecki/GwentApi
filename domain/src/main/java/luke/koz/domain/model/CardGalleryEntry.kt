package luke.koz.domain.model

//todo rework this into general Card class[?]
/**
 * Represents a card entry in the gallery
 */
data class CardGalleryEntry(
    val id: Int,
    val artId: Int,
    val name: String,
    val faction: String,
    val color: String,
    val rarity: String,
    val power: Int,
    val flavor: String,

    val isLiked: Boolean = false,
    val likeCount: Int = 0
)