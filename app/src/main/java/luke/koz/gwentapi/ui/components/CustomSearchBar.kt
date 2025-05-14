package luke.koz.gwentapi.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import luke.koz.gwentapi.domain.model.CardGalleryEntry
import luke.koz.gwentapi.domain.state.SearchState
import luke.koz.gwentapi.ui.cardgalleryscreen.components.CardImageWithBorder
import luke.koz.gwentapi.ui.cardgalleryscreen.components.CardList

@Composable
fun CustomSearchBar(
    query: String,
    searchState: SearchState,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onClose: () -> Unit,
    onCardClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SearchHeader(
            query = query,
            onQueryChanged = onQueryChange,
            onClearQuery = onClearQuery,
            onClose = onClose
        )

        SearchContent(
            searchState = searchState,
            onCardClick = onCardClick
        )
    }
}

@Composable
private fun SearchHeader(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClearQuery: () -> Unit,
    onClose: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.weight(1f),
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = null),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Start typing to search...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    innerTextField()
                }
            }
        )

        // Clear & Close Buttons
        IconButton(onClick = onClearQuery, enabled = query.isNotEmpty()) {
            Icon(Icons.Default.Close, "Clear query")
        }
        IconButton(onClick = onClose) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, "Close search")
        }
    }
}

@Composable
private fun SearchContent(
    searchState: SearchState,
    onCardClick: (Int) -> Unit
) {
    when (searchState) {
        is SearchState.Idle -> PlaceholderContent()
        is SearchState.Loading -> LoadingIndicator()
        is SearchState.Empty -> PlaceholderContent()
        is SearchState.Success -> SuccessStateContent(searchState.results, onCardClick)
        is SearchState.Error -> Text("Error: ${searchState.message}")
    }
}

@Composable
private fun PlaceholderContent() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CardImageWithBorder(cardId = -1, cardColor = "gold")
    }
}

@Composable
private fun LoadingIndicator() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SuccessStateContent(cards: List<CardGalleryEntry>, onCardClick: (Int) -> Unit) {
    CardList(cards, onCardClick)
}

@Composable
private fun ErrorStateContent(message: String) {
    Text("Error: $message")
}