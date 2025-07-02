package luke.koz.navigation

import kotlinx.serialization.Serializable

@Serializable
object CardGalleryDestination

@Serializable
data class CardDetailDestination(val cardId: Int)

@Serializable
object SearchDestination

@Serializable
object AuthDestination

@Serializable
data class CardArtShowcaseDestination (val cardArtId: Int, val cardColor: String)