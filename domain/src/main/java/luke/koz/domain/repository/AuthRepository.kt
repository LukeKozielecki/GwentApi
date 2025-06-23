package luke.koz.domain.repository

import luke.koz.domain.auth.AuthOperationResult
import luke.koz.domain.auth.AuthUserModel

interface AuthRepository {
    suspend fun signIn(email: String, password: String): AuthOperationResult<AuthUserModel>
    suspend fun register(email: String, password: String): AuthOperationResult<AuthUserModel>
    suspend fun getCurrentUser() : AuthUserModel?
    suspend fun signOut()
}