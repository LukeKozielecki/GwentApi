package luke.koz.gwentapi.domain.repository

import luke.koz.gwentapi.domain.model.AuthOperationResult
import luke.koz.gwentapi.domain.model.AuthUserModel

interface AuthRepository {
    suspend fun signIn(email: String, password: String): AuthOperationResult<AuthUserModel>
    suspend fun register(email: String, password: String): AuthOperationResult<AuthUserModel>
    suspend fun getCurrentUser() : AuthUserModel?
}