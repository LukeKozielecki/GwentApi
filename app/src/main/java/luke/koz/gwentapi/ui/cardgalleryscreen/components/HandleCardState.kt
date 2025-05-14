package luke.koz.gwentapi.ui.cardgalleryscreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun HandleCardState(state: CardState, onCardClick : (Int) -> Unit) {
    when (state) {
        is CardState.Empty -> NoCardsAvailable()
        is CardState.Loading -> CircularProgressIndicator()
        is CardState.Success -> CardList(cards = state.cards, onCardClick)
        is CardState.Error -> ErrorMessage(message = state.message)
    }
}

@Composable
private fun CardList(cards: List<CardGalleryEntry>, onCardClick : (Int) -> Unit) {
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    searchState: SearchState,
    onCardClick : (Int) -> Unit,
    closeSearch: () -> Unit
) {
    val searchQuery by remember { mutableStateOf(viewModel.query) }

    Column {
        Row {
            SearchBar(
                query = searchQuery.value,
                onQueryChange = {
                    viewModel.updateQuery(it)
                    viewModel.getCardByQuery()
                },
                placeholder = { Text("Start typing to search...") },
                onSearch = { /* todo */ },
                active = true,
                onActiveChange = {},
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.gwent_one_api_favicon_96x96),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(32.dp)
                    )
                },
                trailingIcon = {
                    Row {
                        ActionIconButton(
                            action = { viewModel.updateQuery("") },
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear query"
                        )
                        ActionIconButton(
                            action = closeSearch,
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Close search"
                        )
                    }
                },
                content = {
                    when (searchState) {
                        is SearchState.Idle -> {
                            CardImageWithBorder(
                                cardId = -1,
                                cardColor = "gold"
                            )
                        }
                        is SearchState.Loading -> CircularProgressIndicator()
                        is SearchState.Empty -> {
                            Row {
                                CardImageWithBorder(
                                    cardId = -1,
                                    cardColor = "gold"
                                )
                                Text("No results found")
                            }
                        }
                        is SearchState.Success -> CardList(searchState.results, onCardClick)
                        is SearchState.Error -> Text("Error: ${searchState.message}")
                    }
                }
            )
        }
    }
}

@Composable
private fun ActionIconButton(action : () -> Unit, imageVector: ImageVector, contentDescription: String) {
    IconButton(onClick = action) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
        )
    }
}