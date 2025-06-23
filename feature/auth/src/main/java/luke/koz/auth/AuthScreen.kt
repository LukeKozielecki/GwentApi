package luke.koz.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import luke.koz.auth.components.AuthContainer
import luke.koz.auth.components.AuthHeader
import luke.koz.auth.viewmodel.AuthState
import luke.koz.auth.viewmodel.AuthViewModel
import luke.koz.auth.viewmodel.provideAuthViewModel
import luke.koz.presentation.scaffold.components.ScaffoldWrapper

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    scaffold: @Composable (NavHostController, @Composable (PaddingValues) -> Unit) -> Unit = { _, content ->
        content(PaddingValues())
    }
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

    ScaffoldWrapper(
        navController = navController,
        scaffold = scaffold
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
            AuthHeader(Modifier.padding(16.dp))
            AuthContainer(
                email = viewModel.email,
                onEmailChange = viewModel::onEmailChange,
                password = viewModel.password,
                onPasswordChange = viewModel::onPasswordChange,
                isEmailValid = viewModel.isEmailValid,
                isPasswordValid = viewModel.isPasswordValid,
                isFormValid = viewModel.isFormValid,
                login = viewModel::login,
                register = viewModel::register,
                authState = authState
            )
        }
    }
}
