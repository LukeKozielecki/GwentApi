package luke.koz.domain.auth

import luke.koz.domain.model.AuthUserModel

/**
 * A sealed class representing the final outcome of an authentication operation,
 * login and registration. Provides a clear, type-safe way to handle authentication outcomes.
 */
sealed class AuthResult {
    data class Success(val user: AuthUserModel) : AuthResult()
    data class Error(val message: String) : AuthResult()
}