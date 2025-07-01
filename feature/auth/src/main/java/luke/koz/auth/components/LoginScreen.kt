package luke.koz.auth.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import luke.koz.auth.model.AuthScreenContentActions
import luke.koz.auth.model.AuthScreenContentUiState
import luke.koz.auth.model.AuthState
import luke.koz.presentation.theme.GwentApiTheme

@Composable
internal fun LoginScreen (
    uiState: AuthScreenContentUiState,
    actions: AuthScreenContentActions,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card(modifier = Modifier.align(Alignment.Center).fillMaxWidth()) {
            AuthHeader(Modifier.padding(16.dp))
            Text(
                text = "Sign in to your GwentApi Compendium account",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp).fillMaxWidth()
            )

            Spacer(Modifier.size(4.dp))

            AuthInputForm(
                uiState = uiState,
                actions = actions,
                modifier = modifier
            )

            Spacer(Modifier.size(8.dp))

            AuthValidationButton(
                uiState = uiState,
                actions = actions,
                modifier = modifier
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPrev() {
    val mock : () -> Unit = {  }
    val mock2 : (String) -> Unit = { _ -> }
    GwentApiTheme {
        LoginScreen(
            uiState = AuthScreenContentUiState(
                email = "email@example.com",
                password = "password123",
                isEmailValid = true,
                isPasswordValid = true,
                isFormValid = true,
                authState = AuthState.Idle,
                currentUser = null,
            ),
            actions = AuthScreenContentActions(
                onEmailChange = mock2,
                onPasswordChange = mock2,
                onLogin = mock,
                onRegister = mock,
                onLogout = mock
            ),
            modifier = Modifier,)
    }
}