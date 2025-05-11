package luke.koz.gwentapi.ui.cardgalleryscreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import luke.koz.gwentapi.domain.model.CardGalleryEntry
import luke.koz.gwentapi.domain.viewModel.CardState

@Composable
fun HandleCardState(state: CardState) {
    when (state) {
        is CardState.Empty -> NoCardsAvailable()
        is CardState.Loading -> CircularProgressIndicator()
        is CardState.Success -> CardList(cards = state.cards)
        is CardState.Error -> ErrorMessage(message = state.message)
    }
}

@Composable
private fun CardList(cards: List<CardGalleryEntry>) {
    if (cards.isEmpty()) {
        NoCardsAvailable()
    } else {
        LazyColumn {
            items(cards) { card ->
                CardItem(card = card)
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun CardItem(card: CardGalleryEntry) {
    Row {
        CardImageWithBorder(card.artId, card.color)
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = card.name, style = MaterialTheme.typography.titleMedium)
            Text(text = card.flavor, style = MaterialTheme.typography.bodySmall)
            Text(text = card.artId.toString(), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Text(
        text = "Error: $message",
        color = Color.Red,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
private fun NoCardsAvailable() {
    Text(
        text = "No cards found",
        color = Color.Red,
        modifier = Modifier.padding(16.dp)
    )
}