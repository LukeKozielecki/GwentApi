package luke.koz.domain.auth

/**
 * A utility object providing validation constants and regular expressions.
 * This object centralizes validation rules for email and password, ensuring
 * user input is valid on presentation layer before being submitted.
 */
object Validation {

    /**
     * Regular expression for validating email addresses.
     * It ensures the email follows a standard format (e.g., "user@example.com").
     */
    val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

    /**
     * The minimum required length for a password.
     */
    const val MIN_PASSWORD_LENGTH = 8
}