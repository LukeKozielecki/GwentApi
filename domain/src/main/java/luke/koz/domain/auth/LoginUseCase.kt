package luke.koz.domain.auth

import luke.koz.domain.model.AuthOperationResult
import luke.koz.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        return when (val result = repository.signIn(email, password)) {
            is AuthOperationResult.Success -> AuthResult.Success(result.data!!)
            is AuthOperationResult.Error -> AuthResult.Error(result.message ?: "Login failed")
            is AuthOperationResult.Loading -> throw IllegalStateException("Unexpected Loading state")
        }
    }
}