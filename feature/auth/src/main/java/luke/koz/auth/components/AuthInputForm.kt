package luke.koz.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import luke.koz.auth.model.AuthFieldState
import luke.koz.auth.model.AuthScreenContentActions
import luke.koz.auth.model.AuthScreenContentUiState

@Composable
internal fun AuthInputForm(
    uiState: AuthScreenContentUiState,
    actions: AuthScreenContentActions,
    modifier: Modifier
) {
    var emailState by remember { mutableStateOf(AuthFieldState()) }
    var passwordState by remember { mutableStateOf(AuthFieldState()) }

    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthTextField(
            value = uiState.email,
            onValueChange = actions.onEmailChange,
            label = "Email",
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            onImeAction = { passwordFocusRequester.requestFocus() },
            focusRequester = emailFocusRequester,
            isValid = uiState.isEmailValid,
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
            value = uiState.password,
            onValueChange = actions.onPasswordChange,
            label = "Password",
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            onImeAction = { focusManager.clearFocus() },
            focusRequester = passwordFocusRequester,
            isValid = uiState.isPasswordValid,
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
    }
}