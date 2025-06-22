package luke.koz.domain.auth

import luke.koz.domain.model.AuthUserModel

sealed class AuthResult {
    data class Success(val user: AuthUserModel) : AuthResult()
    data class Error(val message: String) : AuthResult()
}