package luke.koz.gwentapi.ui.screen.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import luke.koz.gwentapi.ui.model.auth.AuthFieldState
import luke.koz.gwentapi.ui.theme.GwentApiTheme
import luke.koz.gwentapi.ui.viewmodel.AuthState

@Composable
fun AuthContainer(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isEmailValid: Boolean,
    isPasswordValid: Boolean,
    isFormValid: Boolean,
    login: () -> Unit,
    register: () -> Unit,
    authState: AuthState,
    modifier: Modifier = Modifier
) {
    var emailState by remember { mutableStateOf(AuthFieldState()) }
    var passwordState by remember { mutableStateOf(AuthFieldState()) }

    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    Card {
        Text(
            text = "Sign in to your GwentApi Compendium account",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AuthTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Email",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onImeAction = { passwordFocusRequester.requestFocus() },
                focusRequester = emailFocusRequester,
                isValid = isEmailValid,
                isTouched = emailState.isTouched,
                onFocusChanged = { focusState ->
                    emailState = emailState.copy(
                        hasBeenFocused = focusState.isFocused || emailState.hasBeenFocused,
                        isTouched = if (!focusState.isFocused && emailState.hasBeenFocused) true
                        else emailState.isTouched
                    )
                }
            )
            AuthTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = "Password",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                onImeAction = { focusManager.clearFocus() },
                focusRequester = passwordFocusRequester,
                isValid = isPasswordValid,
                isTouched = passwordState.isTouched,
                onFocusChanged = { focusState ->
                    passwordState = passwordState.copy(
                        hasBeenFocused = focusState.isFocused || passwordState.hasBeenFocused,
                        isTouched = if (!focusState.isFocused && passwordState.hasBeenFocused) true
                        else passwordState.isTouched
                    )
                },
                visualTransformation = PasswordVisualTransformation()
            )
            AuthButton(
                text = "Sign in",
                onClick = { login() },
                enabled = isFormValid && authState !is AuthState.Loading,
                isLoading = authState is AuthState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
            AuthButton(
                text = "Register",
                onClick = { register() },
                enabled = isFormValid && authState !is AuthState.Loading,
                isLoading = authState is AuthState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun AuthContainerPrev() {
    val mock : () -> Unit = {  }
    val mock2 : (String) -> Unit = { _ -> }
    GwentApiTheme {
        AuthContainer(
            email = "email",
            onEmailChange = mock2,
            password = "password",
            onPasswordChange = mock2,
            isEmailValid = true,
            isPasswordValid = true,
            isFormValid = true,
            login = mock,
            register = mock,
            authState = AuthState.Idle,
            modifier = Modifier,
        )
    }
}
