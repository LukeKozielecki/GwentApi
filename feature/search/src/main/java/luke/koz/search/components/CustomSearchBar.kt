package luke.koz.search.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.presentation.CardImageWithBorder
import luke.koz.presentation.CardList
import luke.koz.presentation.state.SearchState

@Composable
fun CustomSearchBar(
    query: String,
    searchState: SearchState,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onClose: () -> Unit,
    onCardClick: (Int) -> Unit,
    imageLoader: ImageLoader,
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
            onCardClick = onCardClick,
            imageLoader = imageLoader
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
    onCardClick: (Int) -> Unit,
    imageLoader: ImageLoader
) {
    when (searchState) {
        is SearchState.Idle -> PlaceholderContent(imageLoader = imageLoader)
        is SearchState.Loading -> LoadingIndicator()
        is SearchState.Empty -> PlaceholderContent(imageLoader = imageLoader)
        is SearchState.Success -> SuccessStateContent(searchState.results, onCardClick, imageLoader)
        is SearchState.Error -> Text("Error: ${searchState.message}")
    }
}

@Composable
private fun PlaceholderContent(
    imageLoader: ImageLoader
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CardImageWithBorder(cardId = -1, cardColor = "gold", imageLoader)
    }
}

@Composable
private fun LoadingIndicator() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SuccessStateContent(
    cards: List<CardGalleryEntry>,
    onCardClick: (Int) -> Unit,
    imageLoader: ImageLoader
) {
    //todo rework this to not have this button
    CardList(
        cards = cards,
        onCardClick = onCardClick,
        onToggleLike = { _, _ ->

        },
        imageLoader = imageLoader
    )
}

@Composable
private fun ErrorStateContent(message: String) {
    Text("Error: $message")
}