package luke.koz.gwentapi.ui.cardgalleryscreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import luke.koz.gwentapi.R
import luke.koz.gwentapi.domain.model.CardGalleryEntry
import luke.koz.gwentapi.domain.state.CardState
import luke.koz.gwentapi.domain.state.SearchState
import luke.koz.gwentapi.domain.viewModel.SearchViewModel

@Composable
fun CardStateHandler(
    state: CardState,
    onEmpty: @Composable () -> Unit,
    onLoading: @Composable () -> Unit,
    onSuccess: @Composable (List<CardGalleryEntry>) -> Unit,
    onError: @Composable (String) -> Unit = { ErrorMessage(message = it) }
) {
    when (state) {
        is CardState.Empty -> onEmpty()
        is CardState.Loading -> onLoading()
        is CardState.Success -> onSuccess(state.cards)
        is CardState.Error -> onError(state.message)
    }
}

@Composable
fun HandleCardState(state: CardState, onCardClick : (Int) -> Unit) {
    CardStateHandler(
        state = state,
        onEmpty = { NoCardsAvailable() },
        onLoading = { CircularProgressIndicator() },
        onSuccess = { cards -> CardList(cards = cards, onCardClick) }
    )
}

@Composable
fun HandleDetailsCardState(state: CardState, onCardClick: (Int) -> Unit) {
    CardStateHandler(
        state = state,
        onEmpty = { /* Empty composable */ },
        onLoading = { /* Empty composable */ },
        onSuccess = { cards -> CardItemDetails(card = cards.first(), onCardClick) }
    )
}

@Composable
fun CardList(cards: List<CardGalleryEntry>, onCardClick : (Int) -> Unit) {
    if (cards.isEmpty()) {
        NoCardsAvailable()
    } else {
        LazyColumn {
            items(cards) { card ->
                CardItem(card = card, onCardClick)
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun CardItem(card: CardGalleryEntry, onCardClick : (Int) -> Unit) {
    Row (
        modifier = Modifier
            .clickable { onCardClick(card.id) }
    ) {
        CardImageWithBorder(card.artId, card.color)
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = card.name, style = MaterialTheme.typography.titleMedium)
            Text(text = card.flavor, style = MaterialTheme.typography.bodySmall)
            Text(text = card.artId.toString(), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun CardItemDetails(card: CardGalleryEntry?, onCardClick : (Int) -> Unit) {//todo this onclick should navigate to CardArtShowcaseScreen
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //todo SRP cut
        if (card != null) {
            CardImageWithBorder(card.artId, card.color)
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = card.name, style = MaterialTheme.typography.titleMedium)
                Text(text = card.flavor, style = MaterialTheme.typography.bodySmall)
                Text(text = card.faction, style = MaterialTheme.typography.bodySmall)
                Text(text = card.rarity, style = MaterialTheme.typography.bodySmall)
            }
        } else {
            Column {
                CardImageWithBorder(-1, "gold")
                Text("Ops. Something went wrong")
            }
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

