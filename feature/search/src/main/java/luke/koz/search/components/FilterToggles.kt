package luke.koz.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable for displaying and toggling search filter options: Exact Matches and Approximate Matches.
 */
@Composable
internal fun FilterToggles(
    showExactMatches: Boolean,
    showApproximateMatches: Boolean,
    onToggleExactMatches: (Boolean) -> Unit,
    onToggleApproximateMatches: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = showExactMatches,
                onCheckedChange = onToggleExactMatches
            )
            Text("Exact Matches")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = showApproximateMatches,
                onCheckedChange = onToggleApproximateMatches
            )
            Text("Approximate Matches")
        }
    }
}