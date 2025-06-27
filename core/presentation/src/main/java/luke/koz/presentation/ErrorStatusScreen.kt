package luke.koz.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//todo: as of now, on initial load user should get `No cards found` screen EmptyState.kt
// from `:core:presentation`;
// this composable is called when user tries to like but is not logged in;
// this [ErrorMessage] should be: refactored into toast or the user should be navigated
// to logging in / register screen with a toast telling them what is going on
@Composable
fun ErrorStatusScreen(message: String, onRefreshClick : () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Error: $message",
            color = Color.Red,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = onRefreshClick) {
            Text(text = "Refresh")
        }
    }
}
