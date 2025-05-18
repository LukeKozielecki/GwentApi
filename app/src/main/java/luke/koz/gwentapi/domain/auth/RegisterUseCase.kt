package luke.koz.gwentapi.domain.auth

import luke.koz.gwentapi.domain.model.AuthOperationResult
import luke.koz.gwentapi.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        return when (val result = repository.register(email, password)){
            is AuthOperationResult.Success -> AuthResult.Success(result.data!!)
            is AuthOperationResult.Error -> AuthResult.Error(result.message ?: "Registration failed")
            is AuthOperationResult.Loading -> throw IllegalStateException("Unexpected Loading state")

        }
    }
}