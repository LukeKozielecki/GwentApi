package luke.koz.auth.model

import luke.koz.auth.AuthScreenLayout

/**
 * Represents the single source of truth for actions and callbacks
 * that [AuthScreenLayout] composable can trigger.
 *
 * @property onEmailChange Invoked when the email input changes.
 * @property onPasswordChange Invoked when the password input changes.
 * @property onLogin Invoked when the login button is clicked.
 * @property onRegister Invoked when the register button is clicked.
 * @property onLogout Invoked when the logout button is clicked.
 */
internal data class AuthScreenContentActions(
    val onEmailChange: (String) -> Unit,
    val onPasswordChange: (String) -> Unit,
    val onLogin: () -> Unit,
    val onRegister: () -> Unit,
    val onLogout: () -> Unit
)