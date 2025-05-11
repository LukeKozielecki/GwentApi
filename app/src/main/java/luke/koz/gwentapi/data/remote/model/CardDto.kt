package luke.koz.gwentapi.data.remote.model

// Card-related DTOs
data class CardDto (
    val id : CardIdDto,
    val attributes : CardAttributeDto,
    val name : String,
    val category: String,
    val ability : String,
    val ability_html : String,
    val keyword_html : String,
    val flavor : String
)

data class CardIdDto (
    val art : Int,
    val card : Int,
    val audio : Int
)

data class CardAttributeDto (
    val set : String,
    val type : String,
    val armor : Int,
    val color : String,
    val power : Int,
    val reach : Int,
    val artist : String,
    val rarity : String,
    val faction : String,
    val related : String,
    val provision : Int,
    val factionSecondary : String
)

