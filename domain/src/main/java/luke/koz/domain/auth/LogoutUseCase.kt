package luke.koz.domain.auth

import luke.koz.domain.repository.AuthRepository

/**
 * Contains the business logic for user logging out.
 */
class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): LogoutResult {
        return try {
            repository.signOut()
            LogoutResult.Success
        } catch (e: Exception) {
            LogoutResult.Error(e.message ?: "Unknown logout error")
        }
    }
}
