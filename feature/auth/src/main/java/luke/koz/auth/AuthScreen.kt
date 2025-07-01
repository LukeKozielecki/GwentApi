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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import luke.koz.auth.components.AuthBlock
import luke.koz.auth.components.ProfileScreen
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

