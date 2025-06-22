package luke.koz.domain.auth

object Validation {
    val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
    const val MIN_PASSWORD_LENGTH = 8
}