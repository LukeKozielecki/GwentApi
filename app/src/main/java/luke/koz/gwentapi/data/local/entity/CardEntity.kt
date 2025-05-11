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
