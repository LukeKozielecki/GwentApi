package luke.koz.domain.auth

/**
 * A sealed class representing the final outcome of an logging out operation,
 * Provides a clear, type-safe way to handle logging out outcomes.
 */
sealed class LogoutResult {
    object Success : LogoutResult()
    data class Error(val message: String) : LogoutResult()
}