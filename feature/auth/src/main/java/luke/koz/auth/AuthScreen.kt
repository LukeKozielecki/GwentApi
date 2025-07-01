package luke.koz.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import luke.koz.auth.components.AuthContainer
import luke.koz.auth.components.AuthHeader
import luke.koz.auth.viewmodel.AuthState
import luke.koz.auth.viewmodel.AuthViewModel
import luke.koz.auth.viewmodel.provideAuthViewModel
import luke.koz.domain.auth.AuthUserModel

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: AuthViewModel = provideAuthViewModel()
    val authState = viewModel.authState.value
    val context = LocalContext.current

    val currentUser by viewModel.currentUser.collectAsState()
    val isUserCheckComplete by viewModel.isUserCheckComplete.collectAsState()

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

    LaunchedEffect(isUserCheckComplete, currentUser) {
        if (isUserCheckComplete) {
            if (currentUser != null) {
                Log.d("AuthScreenDebug", "User found on initial check: ${currentUser?.email ?: "error"}")
            } else {
                Log.d("AuthScreenDebug", "No user found on initial check. Showing login screen.")
            }
        } else {
            Log.d("AuthScreenDebug", "Initial user check still in progress...")
        }
    }

    Scaffold { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp), verticalArrangement = Arrangement.Center) {
            if (currentUser == null) {
                AuthBlock(
                    viewModel = viewModel,
                    authState = authState,
                    modifier = modifier
                )
            } else if (currentUser != null) {
                ProfileScreen(
                    user = currentUser!!,
                    onLogout = { viewModel.logout() }
                )
            }
        }
    }
}

@Composable
private fun AuthBlock(
    viewModel: AuthViewModel,
    authState: AuthState,
    modifier: Modifier = Modifier
) {
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


@Composable
fun ProfileScreen(user: AuthUserModel, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(Modifier.padding(16.dp))
        Card(modifier = Modifier
            .fillMaxWidth()
        ) {
            Column (Modifier
                .fillMaxWidth()
                .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text("Welcome, ${user.email}", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onLogout) {
                    Text("Logout")
                }
            }
        }
    }
}