package luke.koz.gwentapi.data.mapper

import luke.koz.gwentapi.data.local.entity.CardEntity
import luke.koz.gwentapi.domain.model.CardGalleryEntry


fun CardEntity.toDomain(): CardGalleryEntry {
    return CardGalleryEntry(
        id = this.cardId,
        artId = this.artId,
        name = this.name,
        faction = this.faction,
        color = this.color,
        rarity = this.rarity,
        power = this.power,
        flavor = this.flavor
    )
}