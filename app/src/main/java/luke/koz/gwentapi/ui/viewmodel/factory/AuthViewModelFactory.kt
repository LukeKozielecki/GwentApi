package luke.koz.gwentapi.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import luke.koz.gwentapi.domain.auth.LoginUseCase
import luke.koz.gwentapi.domain.auth.RegisterUseCase
import luke.koz.gwentapi.domain.repository.AuthRepository
import luke.koz.gwentapi.ui.viewmodel.AuthViewModel

class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(repository) as T
    }
}