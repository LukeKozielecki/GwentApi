package luke.koz.domain.model

//todo this will not work if feature that allows language selection for user is implemented
/**
 * Represents possible types of a card.
 * Facilitates type-safe equality checks on objects derivative from `CardEntity.type`.
 */
enum class CardAttributeType(val type: String) {
    SPECIAL("Special"),
    ARTIFACT("Artifact"),
    UNIT("Unit");
}