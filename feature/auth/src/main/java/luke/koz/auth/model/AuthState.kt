package luke.koz.auth.model

import luke.koz.domain.auth.AuthUserModel

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: AuthUserModel) : AuthState()
    data class Error(val message: String) : AuthState()
}