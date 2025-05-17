package luke.koz.gwentapi.domain.model

class CardDetailsEntry(
    //Not displayed
    val cardId: Int,
    val artId: Int,
    val audioId: Int,
    //Flavour
    val name: String,
    val category: String,
    val abilityHtml: String,//todo move down this class to stats
    val flavor: String,
    val color: String,
    val rarity: String,
    //Stats
    val type: String,
    val armor: Int,
    val power: Int,
    val reach: Int, //apparently they don't use this anymore
    val artist: String,
    val faction: String,
    val related: String,
    val provision: Int,
    //Dictionary
    val keywordHtml: String,

)