package luke.koz.gwentapi.ui

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import luke.koz.gwentapi.application.GwentApplication
import luke.koz.gwentapi.data.datasource.CardLocalDataSource
import luke.koz.gwentapi.data.datasource.CardRemoteDataSource
import luke.koz.gwentapi.data.remote.api.ApiClient
import luke.koz.gwentapi.data.repository.CardRepository
import luke.koz.gwentapi.domain.model.CardGalleryEntry
import luke.koz.gwentapi.domain.viewModel.CardState
import luke.koz.gwentapi.domain.viewModel.CardViewModel
import luke.koz.gwentapi.domain.viewModel.ViewModelFactory
import luke.koz.gwentapi.ui.components.CardImageWithBorder

@Composable
fun CardEntryHome(cardId: Int) {
    val viewModel: CardViewModel = provideCardViewModel()
    val cardState by viewModel.cardState

    LaunchedEffect(key1 = cardId) {
        viewModel.getAllCards()
    }

    HandleCardState(cardState)
}

@Composable
private fun provideCardViewModel(): CardViewModel {
    val context = LocalContext.current
    val application = context.applicationContext as GwentApplication
    val repository = remember {
        CardRepository(
            remote = CardRemoteDataSource(ApiClient.apiService),
            local = CardLocalDataSource(application.database.cardDao())
        )
    }
    return viewModel(factory = ViewModelFactory(repository))
}

@Composable
private fun HandleCardState(state: CardState) {
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