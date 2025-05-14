package luke.koz.gwentapi.ui.components

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
import luke.koz.gwentapi.domain.viewModel.SearchViewModel
import luke.koz.gwentapi.ui.cardgalleryscreen.components.SearchScreen
import luke.koz.gwentapi.ui.cardgalleryscreen.di.provideSearchGalleryViewModel
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
    if(isSearchActive.value) {
        SearchScreen(
            viewModel = searchViewModel,
            searchState = searchState,
            onCardClick = {/*Todo implement onclick*/},
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
                    isSearchActive.value = !isSearchActive.value
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