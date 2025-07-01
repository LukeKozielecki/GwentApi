package luke.koz.auth.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import luke.koz.auth.viewmodel.AuthState
import luke.koz.auth.viewmodel.AuthViewModel


@Composable
fun AuthBlock(
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
