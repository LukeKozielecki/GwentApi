package luke.koz.auth.model

import luke.koz.auth.AuthScreenLayout
import luke.koz.auth.viewmodel.AuthState
import luke.koz.domain.auth.AuthUserModel

/**
 * Represents the single source of truth for UI state required by the [AuthScreenLayout] composable.
 *
 * @property email The current email input value.
 * @property password The current password input value.
 * @property isEmailValid Indicates if the email input is currently valid.
 * @property isPasswordValid Indicates if the password input is currently valid.
 * @property isFormValid Indicates if the entire authentication form is valid.
 * @property authState The current authentication state [AuthState]
 * @property currentUser The current Authenticated user. If none is, it should be `null`
 */
internal data class AuthScreenContentUiState(
    val email: String,
    val password: String,
    val isEmailValid: Boolean,
    val isPasswordValid: Boolean,
    val isFormValid: Boolean,
    val authState: AuthState,
    val currentUser: AuthUserModel?
)