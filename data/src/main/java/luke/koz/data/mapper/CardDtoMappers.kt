/**
 * Converts [CardDto] (API response objects) to domain models
 */
package luke.koz.data.mapper

import luke.koz.data.local.entity.CardEntity
import luke.koz.data.remote.model.CardDto
import luke.koz.domain.model.CardGalleryEntry

fun CardDto.toCardGalleryEntry(): CardGalleryEntry {
    return CardGalleryEntry(
        id = this.id.card,
        artId = this.id.art,
        name = this.name,
        faction = this.attributes.faction,
        color = this.attributes.color,
        rarity = this.attributes.rarity,
        power = this.attributes.power,
        flavor = this.flavor
    )
}

fun CardDto.toCardEntity(): CardEntity {
    return CardEntity(
        // CardIdDto mapping
        cardId = this.id.card,
        artId = this.id.art,
        audioId = this.id.audio,

        // CardDto direct fields
        name = this.name,
        category = this.category,
        ability = this.ability,
        abilityHtml = this.ability_html,
        keywordHtml = this.keyword_html,
        flavor = this.flavor,

        // CardAttributeDto mapping
        setType = this.attributes.set,
        type = this.attributes.type,
        armor = this.attributes.armor,
        color = this.attributes.color,
        power = this.attributes.power,
        reach = this.attributes.reach,
        artist = this.attributes.artist,
        rarity = this.attributes.rarity,
        faction = this.attributes.faction,
        related = this.attributes.related,
        provision = this.attributes.provision,
        secondaryFaction = this.attributes.factionSecondary
    )
}