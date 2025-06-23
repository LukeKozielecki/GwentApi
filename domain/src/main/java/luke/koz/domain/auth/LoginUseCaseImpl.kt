package luke.koz.domain.auth

import luke.koz.domain.model.AuthOperationResult
import luke.koz.domain.repository.AuthRepository

interface LoginUseCase {
    suspend operator fun invoke(email: String, password: String): AuthResult
}

/**
 * Contains the business logic for user logging in. Orchestrates the authentication process
 * by interacting with the [AuthRepository] and provides a clear result ([AuthResult])
 * to the presentation layer [AuthViewModel].
 */
class LoginUseCaseImpl(private val repository: AuthRepository) : LoginUseCase {
    override suspend operator fun invoke(email: String, password: String): AuthResult {
        return when (val result = repository.signIn(email, password)) {
            is AuthOperationResult.Success -> AuthResult.Success(result.data!!)
            is AuthOperationResult.Error -> AuthResult.Error(result.message ?: "Login failed")
            is AuthOperationResult.Loading -> throw IllegalStateException("Unexpected Loading state")
        }
    }
}