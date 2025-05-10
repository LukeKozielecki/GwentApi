package luke.koz.gwentapi.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import luke.koz.gwentapi.domain.viewModel.CardState
import luke.koz.gwentapi.domain.viewModel.CardViewModel

@Composable
fun CardEntryHome(cardId: Int) {
    val viewModel: CardViewModel = remember { CardViewModel() }
    val cardState by viewModel.cardState

    LaunchedEffect(key1 = cardId) {
        viewModel.getCardById(cardId)
    }

    when (val state = cardState) {
        is CardState.Empty -> {
            // Initial state
        }
        is CardState.Loading -> {
            CircularProgressIndicator()
        }
        is CardState.Success -> {
            // Print mock card details to composable
            Column {
                Text(text = state.card.name)
                Text(text = state.card.flavor)
            }
        }
        is CardState.Error -> {
            Text(text = "Error: ${state.message}", color = Color.Red)
        }
    }
}