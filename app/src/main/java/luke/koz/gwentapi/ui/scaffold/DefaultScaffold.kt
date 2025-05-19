package luke.koz.gwentapi.ui.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import luke.koz.gwentapi.navigation.AuthDestination

@Composable
fun DefaultScaffold(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            GwentTopAppBar(
                onProfileClicked = { navController.navigate(AuthDestination) },
                navController = navController
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}