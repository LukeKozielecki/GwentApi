package luke.koz.domain.auth.usecase

import luke.koz.domain.auth.AuthResult

interface RegisterUseCase {
    suspend operator fun invoke(email: String, password: String): AuthResult
}
