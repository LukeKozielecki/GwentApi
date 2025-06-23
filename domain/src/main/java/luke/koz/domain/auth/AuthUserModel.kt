package luke.koz.domain.auth

/**
 * Represents a simplified user model for authentication purposes.
 *
 * @property id The unique identifier for the authenticated user, here it would be Firebase Auth UID.
 * @property email The email address associated with the authenticated user.
 */
data class AuthUserModel(
    val id: String,
    val email: String
)