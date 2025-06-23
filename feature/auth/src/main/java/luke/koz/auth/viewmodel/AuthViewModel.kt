package luke.koz.auth.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import luke.koz.domain.auth.AuthResult
import luke.koz.domain.auth.LoginUseCase
import luke.koz.domain.auth.RegisterUseCase
import luke.koz.domain.auth.Validation
import luke.koz.domain.model.AuthUserModel
import luke.koz.domain.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _authState = mutableStateOf<AuthState>(AuthState.Idle)
    val authState: State<AuthState> = _authState

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    val isEmailValid get() = email.matches(Validation.EMAIL_REGEX)
    val isPasswordValid get() = password.length >= Validation.MIN_PASSWORD_LENGTH
    val isFormValid get() = isEmailValid && isPasswordValid

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun login() {
        if (!isFormValid) return
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = LoginUseCase(authRepository).invoke(email, password)
            handleAuthResult(result)
        }
    }
    fun register() {
        if (!isFormValid) return
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = RegisterUseCase(authRepository).invoke(email, password)
            handleAuthResult(result)
        }
    }

    private fun handleAuthResult(result: AuthResult) {
        when (result) {
            is AuthResult.Success -> _authState.value = AuthState.Success(result.user)
            is AuthResult.Error -> _authState.value = AuthState.Error(result.message)
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: AuthUserModel) : AuthState()
    data class Error(val message: String) : AuthState()
}
