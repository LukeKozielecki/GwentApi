package luke.koz.gwentapi.ui.screen.sharedcomponents

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import luke.koz.gwentapi.R
import luke.koz.gwentapi.ui.viewmodel.SearchViewModel
import luke.koz.gwentapi.navigation.SearchDestination
import luke.koz.gwentapi.di.provideSearchGalleryViewModel
import luke.koz.gwentapi.ui.screen.search.SearchScreen
import luke.koz.gwentapi.ui.theme.GwentApiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GwentTopAppBar(
    onProfileClicked: () -> Unit,
    navController: NavHostController
) {
    val searchViewModel: SearchViewModel = provideSearchGalleryViewModel()
    val searchState by searchViewModel.searchState
    val isSearchActive = remember { mutableStateOf(false) }
    val searchQuery by remember { mutableStateOf(searchViewModel.query) }
    if(isSearchActive.value) {
        SearchScreen(
            query = searchViewModel.query,
            updateQuery = { searchViewModel.updateQuery(it) },
            getCardByQuery = { searchViewModel.getCardByQuery() },
            searchState = searchState,
            onCardClick = {/*Todo implement onclick*/ },
            closeSearch = { isSearchActive.value = !isSearchActive.value },
        )
    }
    else {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = { /*TODO*/} ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu"
                    )
                }
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.gwent_one_api_favicon_96x96),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "Gwent API",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    val currentRoute = navController.currentBackStackEntry?.destination?.route
                    val searchRoute = SearchDestination::class.qualifiedName
                    if (currentRoute != searchRoute) {
                        navController.navigate(SearchDestination) {
                            launchSingleTop = true
                        }
                    }
//                    isSearchActive.value = !isSearchActive.value
                    Log.d("TopAppBar", "isSearchActive: $isSearchActive")
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
                IconButton(onClick = onProfileClicked) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Profile"
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun GwentTopAppBarPrev() {
    GwentApiTheme {
        GwentTopAppBar(
            onProfileClicked = {  },
            navController = rememberNavController()
        )
    }
}