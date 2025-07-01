package luke.koz.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import luke.koz.auth.model.AuthScreenContentActions
import luke.koz.auth.model.AuthScreenContentUiState
import luke.koz.auth.viewmodel.AuthState
import luke.koz.auth.viewmodel.AuthViewModel
import luke.koz.auth.viewmodel.provideAuthViewModel

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: AuthViewModel = provideAuthViewModel()
    val authState = viewModel.authState.value
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> onAuthSuccess()
            is AuthState.Error -> {
                Log.d("AuthGwent", authState.message)
                Toast.makeText(context, authState.message, Toast.LENGTH_SHORT).show()
                viewModel.resetAuthState()
            }

            AuthState.Loading, AuthState.Idle -> { /* Do nothing */ }
        }
    }

    Scaffold { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.Center) {
            AuthScreenLayout(
                uiState = AuthScreenContentUiState(
                    email = viewModel.email,
                    password = viewModel.password,
                    isEmailValid = viewModel.isEmailValid,
                    isPasswordValid = viewModel.isPasswordValid,
                    isFormValid = viewModel.isFormValid,
                    authState = authState,
                    currentUser = viewModel.currentUser.collectAsState().value,
                ),
                actions = AuthScreenContentActions(
                    onEmailChange = viewModel::onEmailChange,
                    onPasswordChange = viewModel::onPasswordChange,
                    onLogin = viewModel::login,
                    onRegister = viewModel::register,
                    onLogout = viewModel::logout
                ),
                modifier = modifier
            )
        }
    }
}

