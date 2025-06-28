package luke.koz.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.presentation.scaffold.DefaultScaffold
import luke.koz.presentation.state.SearchState
import luke.koz.presentation.theme.GwentApiTheme
import luke.koz.search.components.CustomSearchBar

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
    imageLoader: ImageLoader,
    navController: NavHostController,
    //todo this screen takes parameters navController and scaffold which it doesn't need in current iteration, rethink this composable and rework/remove unused props
    scaffold: @Composable (NavHostController, @Composable (PaddingValues) -> Unit) -> Unit = { nav, content ->
        DefaultScaffold(navController = nav, content = content)
    }
) {
    Scaffold { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            CustomSearchBar(
                query = query.value,
                searchState = searchState,
                onQueryChange = {
                    updateQuery(it)
                    getCardByQuery()
                },
                onClearQuery = { updateQuery("") },
                onClose = closeSearch,
                imageLoader = imageLoader,
                onCardClick = onCardClick,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSearchScreen() {
    val mockNavController: NavHostController = rememberNavController()
    val previewImageLoader = ImageLoader.Builder(LocalContext.current).build()
    GwentApiTheme {
        val mockQuery = remember { mutableStateOf("Ciri") }
        SearchScreen(
            query = mockQuery,
            updateQuery = { },
            getCardByQuery = { },
            searchState = SearchState.Success(
                results = listOf(
                    CardGalleryEntry(
                        id = 112101,
                        artId = 1007,
                        name = "Ciri",
                        faction = "",
                        color = "Gold",
                        rarity = "Legendary",
                        power = 5,
                        flavor = "I go wherever I please, whenever I please."
                    )
                )
            ),
            onCardClick = { },
            closeSearch = { },
            imageLoader = previewImageLoader,
            navController = mockNavController
        )
    }
}