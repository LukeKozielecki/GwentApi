/**
 * Converts [CardDto] (API response objects) to domain models
 */
package luke.koz.gwentapi.data.remote.model

import luke.koz.gwentapi.domain.model.CardGalleryEntry

fun CardDto.toDomain(): CardGalleryEntry {
    return CardGalleryEntry(
        id = this.id.card,
        name = this.name,
        faction = this.attributes.faction,
        rarity = this.attributes.rarity,
        power = this.attributes.power,
        flavor = this.flavor
    )
}