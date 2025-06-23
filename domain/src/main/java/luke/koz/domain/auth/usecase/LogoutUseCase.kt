package luke.koz.domain.auth.usecase

import luke.koz.domain.auth.LogoutResult

interface LogoutUseCase {
    suspend operator fun invoke(): LogoutResult
}
