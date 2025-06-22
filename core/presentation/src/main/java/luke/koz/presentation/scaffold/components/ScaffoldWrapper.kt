package luke.koz.presentation.scaffold.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import luke.koz.presentation.scaffold.DefaultScaffold

@Composable
fun ScaffoldWrapper(
    navController: NavHostController,
    scaffold: @Composable (NavHostController, @Composable (PaddingValues) -> Unit) -> Unit = { nav, content ->
        DefaultScaffold(navController = nav, content = content)
    },
    coreComposable: @Composable () -> Unit
) {
    scaffold(navController) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            coreComposable()
        }
    }
}