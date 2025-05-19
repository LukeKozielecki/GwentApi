package luke.koz.gwentapi.ui.screen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import luke.koz.gwentapi.domain.model.CardGalleryEntry
import luke.koz.gwentapi.ui.scaffold.DefaultScaffold
import luke.koz.gwentapi.ui.scaffold.components.ScaffoldWrapper
import luke.koz.gwentapi.ui.state.SearchState
import luke.koz.gwentapi.ui.theme.GwentApiTheme

/* todo change this scaffold, so that custom search app bar is inside the scaffold
    refactor CustomSearchBar to have only result
    rename CustomSearchBar to be ~~ SearchResult
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    query: State<String>,
    updateQuery: (String) -> Unit,
    getCardByQuery: () -> Unit,
    searchState: SearchState,
    onCardClick : (Int) -> Unit,
    closeSearch: () -> Unit,
    navController: NavHostController,
    scaffold: @Composable (NavHostController, @Composable (PaddingValues) -> Unit) -> Unit = { nav, content ->
        DefaultScaffold(navController = nav, content = content)
    }
) {
    ScaffoldWrapper(navController, scaffold) {
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
    val mockNavController: NavHostController = rememberNavController()
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
            onCardClick = {  },
            closeSearch = {  },
            navController = mockNavController
        )
    }
}