package luke.koz.gwentapi.ui.carddetailsscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CardDetailScreen(
    cardId : Int,
    onBack : () -> Unit
) {
    Column {
        Text("Not implemented, ops. Have a cardId: $cardId")
        Button(onClick = onBack) {
            Text("Back to Gallery")
        }
    }
}