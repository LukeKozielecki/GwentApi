package luke.koz.domain.model

data class CardSearchResult(
    val exactMatches: List<CardGalleryEntry>,
    val approximateMatches: List<CardGalleryEntry>
)
