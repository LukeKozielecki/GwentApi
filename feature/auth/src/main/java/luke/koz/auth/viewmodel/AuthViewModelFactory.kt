package luke.koz.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import luke.koz.domain.auth.LoginUseCase
import luke.koz.domain.auth.LogoutUseCase
import luke.koz.domain.auth.RegisterUseCase
import luke.koz.domain.repository.AuthRepository

class AuthViewModelFactory(
    private val repository: AuthRepository,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(
                repository,
                loginUseCase,
                registerUseCase,
                logoutUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}