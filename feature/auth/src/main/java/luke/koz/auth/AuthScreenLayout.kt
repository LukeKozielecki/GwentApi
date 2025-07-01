package luke.koz.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import luke.koz.auth.components.LoginScreen
import luke.koz.auth.components.ProfileScreen
import luke.koz.auth.model.AuthScreenContentActions
import luke.koz.auth.model.AuthScreenContentUiState

@Composable
internal fun AuthScreenLayout(
    uiState: AuthScreenContentUiState,
    actions: AuthScreenContentActions,
    modifier: Modifier = Modifier
) {
    if (uiState.currentUser == null) {
        LoginScreen(
            uiState = uiState,
            actions = actions
        )
    } else {
        ProfileScreen(
            user = uiState.currentUser,
            onLogout = actions.onLogout
        )
    }
}
