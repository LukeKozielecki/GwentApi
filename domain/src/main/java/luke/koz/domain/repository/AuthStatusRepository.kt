package luke.koz.domain.repository

import kotlinx.coroutines.flow.Flow
import luke.koz.domain.auth.AuthUserModel

/**
 * Interface for observing the current authentication status of a user.
 * Provides a stream of authenticated user models.
 */
interface AuthStatusRepository {
    /**
     * Observes the current authenticated user.
     * Emits `AuthUserModel` if a user is logged in, or `null` if no user is authenticated.
     *
     * @return A [Flow] emitting the current [AuthUserModel] or `null`.
     */
    fun observeCurrentUser(): Flow<AuthUserModel?>
}