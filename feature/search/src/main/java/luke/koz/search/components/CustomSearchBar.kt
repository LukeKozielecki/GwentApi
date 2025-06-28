package luke.koz.search.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
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
import coil3.ImageLoader
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.presentation.card.CardList
import luke.koz.presentation.state.SearchState
import luke.koz.presentation.statusscreen.ErrorStatusScreen
import luke.koz.presentation.statusscreen.LoadingStatusScreen
import luke.koz.presentation.statusscreen.NoDataStatusScreen
import luke.koz.presentation.statusscreen.SuccessStatusScreen

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
        is SearchState.Idle -> NoDataStatusScreen(
            emptyStateDescription = "",
            toastMessage = null
        )
        is SearchState.Loading -> LoadingStatusScreen()
        is SearchState.Empty -> NoDataStatusScreen(
            emptyStateDescription = "No cards found for searched query",
            toastMessage = null
        )
        is SearchState.Success -> SuccessStatusScreen {
            SearchResultsList(
                searchState.results,
                onCardClick,
                imageLoader
            )
        }
        is SearchState.Error -> ErrorStatusScreen(
            "Error: ${searchState.message}",
            onRefreshClick = { },
        )
    }
}

@Composable
private fun SearchResultsList(
    cards: List<CardGalleryEntry>,
    onCardClick: (Int) -> Unit,
    imageLoader: ImageLoader
) {
    //todo rework this to not have this button
    SuccessStatusScreen(
        content = {
            CardList(
                cards = cards,
                onCardClick = onCardClick,
                onToggleLike = { _, _ ->

                },
                imageLoader = imageLoader
            )
        }
    )
}