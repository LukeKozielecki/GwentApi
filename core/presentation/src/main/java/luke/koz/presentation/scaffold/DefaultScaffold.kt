package luke.koz.presentation.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import luke.koz.navigation.AuthDestination
import luke.koz.navigation.SearchDestination

@Composable
fun DefaultScaffold(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val onSearchClicked = {
        navController.navigate(SearchDestination) {
            launchSingleTop = true
        }
    }
    val onProfileClicked = {
        navController.navigate(AuthDestination)
    }
    Scaffold(
        topBar = {
            GwentTopAppBar(
                onProfileClicked = onProfileClicked,
                onSearchClicked = onSearchClicked
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}