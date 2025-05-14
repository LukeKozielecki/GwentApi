package luke.koz.gwentapi.ui.components

import androidx.compose.runtime.State
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import luke.koz.gwentapi.R
import luke.koz.gwentapi.domain.model.CardGalleryEntry
import luke.koz.gwentapi.domain.state.SearchState
import luke.koz.gwentapi.ui.cardgalleryscreen.components.CardImageWithBorder
import luke.koz.gwentapi.ui.cardgalleryscreen.components.CardList
import luke.koz.gwentapi.ui.theme.GwentApiTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    query: State<String>,
    updateQuery: (String) -> Unit,
    getCardByQuery: () -> Unit,
    searchState: SearchState,
    onCardClick : (Int) -> Unit,
    closeSearch: () -> Unit
) {
    Column {
        CustomSearchBar(
            query = query.value,
            searchState = searchState,
            onQueryChange = {
                updateQuery(it)
                getCardByQuery()
            },
            onClearQuery = { updateQuery("") },
            onClose = closeSearch,
            onCardClick = onCardClick,
        )

        //todo delete this block
        /*SearchBar(
            query = query.value,
            onQueryChange = {
                updateQuery(it)
                getCardByQuery()
            },
            placeholder = { Text("Start typing to search...") },
            onSearch = { *//* todo *//* },
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
                        action = { updateQuery("") },
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
            },
            modifier = Modifier.statusBarsPadding()
        )*/
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

@Preview
@Composable
private fun PreviewSearchScreen() {
    GwentApiTheme {
        val mockQuery = remember { mutableStateOf("Ciri") }
        SearchScreen(
            query = mockQuery,
            updateQuery = {  },
            getCardByQuery = {  },
            searchState = SearchState.Success(
                results = listOf(CardGalleryEntry(
                    id = 112101,
                    artId = 1007,
                    name = "Ciri",
                    faction = "",
                    color = "Gold",
                    rarity = "Legendary",
                    power = 5,
                    flavor = "I go wherever I please, whenever I please."
                ))
            ),
            onCardClick = {  }
        ) { }
    }
}