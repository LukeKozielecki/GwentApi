package luke.koz.domain.model

data class CardGalleryData(
    val rawCards: List<CardGalleryEntry>,
    val likedCardIds: Set<Int>,
    val allCardLikes: Map<Int, Set<String>>
)