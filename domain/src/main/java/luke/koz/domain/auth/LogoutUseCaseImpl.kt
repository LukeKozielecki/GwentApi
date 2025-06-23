package luke.koz.domain.auth

import luke.koz.domain.repository.AuthRepository

interface LogoutUseCase {
    suspend operator fun invoke(): LogoutResult
}

/**
 * Contains the business logic for user logging out.
 */
class LogoutUseCaseImpl(private val repository: AuthRepository): LogoutUseCase {
    override suspend operator fun invoke(): LogoutResult {
        return try {
            repository.signOut()
            LogoutResult.Success
        } catch (e: Exception) {
            LogoutResult.Error(e.message ?: "Unknown logout error")
        }
    }
}
