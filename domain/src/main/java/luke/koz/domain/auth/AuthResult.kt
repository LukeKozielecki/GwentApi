package luke.koz.domain.auth

/**
 * A sealed class representing the final outcome of an authentication operation,
 * login and registration. Provides a clear, type-safe way to handle authentication outcomes.
 */
sealed class AuthResult {
    data class Success(val user: AuthUserModel) : AuthResult()
    data class Error(val message: String) : AuthResult()
}