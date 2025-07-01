package luke.koz.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import luke.koz.domain.auth.AuthUserModel
import luke.koz.presentation.theme.GwentApiTheme

@Composable
fun ProfileScreen(user: AuthUserModel, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(modifier = Modifier
            .fillMaxWidth()
        ) {
            AuthHeader(Modifier.padding(16.dp))
            Column (
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Welcome, ${user.email}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(16.dp))
                Button(onClick = onLogout) {
                    Text("Logout")
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPrev() {
    GwentApiTheme { 
        ProfileScreen(
            user = AuthUserModel(
                id = "b5a5be47-df3e-4dd8-b962-ac483562faa1",
                email = "random.user@server.domain",
            ),
            onLogout = {  }
        )
    }
}