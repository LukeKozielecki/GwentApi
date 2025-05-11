package luke.koz.gwentapi.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import luke.koz.gwentapi.data.remote.model.CardDto
import luke.koz.gwentapi.domain.model.CardGalleryEntry

@Entity(
    tableName = "cards",
    indices = [
        Index("faction"),
        Index("rarity"),
        Index("last_updated")
    ]
)
data class CardEntity(
    // From CardIdDto
    @PrimaryKey
    @ColumnInfo(name = "card_id")
    val cardId: Int,
    @ColumnInfo(name = "art_id")
    val artId: Int,
    @ColumnInfo(name = "audio_id")
    val audioId: Int,

    // From CardDto
    val name: String,
    val category: String,
    val ability: String,
    @ColumnInfo(name = "ability_html")
    val abilityHtml: String,
    @ColumnInfo(name = "keyword_html")
    val keywordHtml: String,
    val flavor: String,

    // From CardAttributeDto
    @ColumnInfo(name = "set_type")
    val setType: String,
    val type: String,
    val armor: Int,
    val color: String,
    val power: Int,
    val reach: Int,
    val artist: String,
    val rarity: String,
    val faction: String,
    val related: String,
    val provision: Int,
    @ColumnInfo(name = "secondary_faction")
    val secondaryFaction: String,

    // Metadata
    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis()
)

fun CardDto.toEntity(): CardEntity {
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

fun CardEntity.toDomain(): CardGalleryEntry {
    return CardGalleryEntry(
        id = this.cardId,
        name = this.name,
        faction = this.faction,
        rarity = this.rarity,
        power = this.power,
        flavor = this.flavor
    )
}