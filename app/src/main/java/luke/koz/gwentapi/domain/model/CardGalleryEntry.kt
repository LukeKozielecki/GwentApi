package luke.koz.gwentapi.domain.model
/**
 * Represents a card entry in the gallery.
 * @see luke.koz.gwentapi.data.remote.model.CardDtoMappers.toDomain
 */
data class CardGalleryEntry(//todo rework this into general Card class[?]
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