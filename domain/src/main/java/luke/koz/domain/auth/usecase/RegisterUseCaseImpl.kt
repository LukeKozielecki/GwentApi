package luke.koz.domain.auth.usecase

import luke.koz.domain.auth.AuthResult
import luke.koz.domain.auth.AuthOperationResult
import luke.koz.domain.repository.AuthRepository

/**
 * Contains the business logic for user registration. Orchestrates the registration process
 * by interacting with the [AuthRepository] and provides a clear result ([AuthResult])
 * to the presentation layer [AuthViewModel].
 */
class RegisterUseCaseImpl(private val repository: AuthRepository) : RegisterUseCase {
    override suspend operator fun invoke(email: String, password: String): AuthResult {
        return when (val result = repository.register(email, password)){
            is AuthOperationResult.Success -> AuthResult.Success(result.data!!)
            is AuthOperationResult.Error -> AuthResult.Error(
                result.message ?: "Registration failed"
            )
            is AuthOperationResult.Loading -> throw IllegalStateException("Unexpected Loading state")

        }
    }
}