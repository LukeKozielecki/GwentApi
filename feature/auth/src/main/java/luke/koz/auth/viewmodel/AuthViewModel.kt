package luke.koz.auth.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import luke.koz.domain.auth.AuthResult
import luke.koz.domain.auth.usecase.LoginUseCase
import luke.koz.domain.auth.LogoutResult
import luke.koz.domain.auth.usecase.LogoutUseCase
import luke.koz.domain.auth.usecase.RegisterUseCase
import luke.koz.domain.auth.Validation
import luke.koz.domain.auth.AuthUserModel
import luke.koz.domain.repository.AuthRepository

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _authState = mutableStateOf<AuthState>(AuthState.Idle)
    val authState: State<AuthState> = _authState

    private val _currentUser = MutableStateFlow<AuthUserModel?>(null)
    val currentUser: StateFlow<AuthUserModel?> = _currentUser.asStateFlow()

    private val _isUserCheckComplete = MutableStateFlow(false)
    val isUserCheckComplete: StateFlow<Boolean> = _isUserCheckComplete.asStateFlow()

    init {
        checkCurrentUserOnInit()
    }

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
            //todo usecase should come via dependency
            val result = loginUseCase.invoke(email, password)
            handleAuthResult(result)
        }
    }
    fun register() {
        if (!isFormValid) return
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            //todo usecase should come via dependency
            val result = registerUseCase.invoke(email, password)
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


    private fun checkCurrentUserOnInit() {
        viewModelScope.launch {
            _currentUser.value = authRepository.getCurrentUser()
            _isUserCheckComplete.value = true
        }
    }

    fun logout() {
        viewModelScope.launch {
            when (val result = logoutUseCase.invoke()) {
                is LogoutResult.Success -> {
                    _currentUser.value = null
                    _authState.value = AuthState.Idle
                    Log.d("AuthViewModel", "User logged out successfully.")
                }
                is LogoutResult.Error -> {
                    Log.e("AuthViewModel", "Logout failed: ${result.message}")
                    _authState.value = AuthState.Error(result.message)
                }
            }
        }
    }
}

//todo extract AuthState to model
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: AuthUserModel) : AuthState()
    data class Error(val message: String) : AuthState()
}
