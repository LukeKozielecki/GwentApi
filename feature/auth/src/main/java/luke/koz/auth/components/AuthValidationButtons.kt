package luke.koz.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import luke.koz.auth.model.AuthScreenContentActions
import luke.koz.auth.model.AuthScreenContentUiState
import luke.koz.auth.model.AuthState

@Composable
internal fun AuthValidationButton(
    uiState: AuthScreenContentUiState,
    actions: AuthScreenContentActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AuthButton(
            text = "Sign in",
            onClick = actions.onLogin,
            enabled = uiState.isFormValid && uiState.authState !is AuthState.Loading,
            isLoading = uiState.authState is AuthState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
        )
        AuthButton(
            text = "Register",
            onClick = actions.onRegister,
            enabled = uiState.isFormValid && uiState.authState !is AuthState.Loading,
            isLoading = uiState.authState is AuthState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
        )
    }
}