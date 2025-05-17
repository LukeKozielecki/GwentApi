package luke.koz.gwentapi.data.mapper

import luke.koz.gwentapi.data.local.entity.CardEntity
import luke.koz.gwentapi.domain.model.CardDetailsEntry
import luke.koz.gwentapi.domain.model.CardGalleryEntry

//todo rename this to toCardGalleryEntry()
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
// todo incorporate this across Details feature
fun CardEntity.toCardDetailsEntry(): CardDetailsEntry {
    return CardDetailsEntry(
        cardId = this.cardId,
        artId = this.artId,
        audioId = this.audioId,
        name = this.name,
        category = this.category,
        abilityHtml = this.ability,
        flavor = this.flavor,
        color = this.color,
        rarity = this.rarity,
        type = this.type,
        armor = this.armor,
        power = this.power,
        reach = this.reach,
        artist = this.artist,
        faction = this.faction,
        related = this.related,
        provision = this.provision,
        keywordHtml = this.keywordHtml,
    )
}