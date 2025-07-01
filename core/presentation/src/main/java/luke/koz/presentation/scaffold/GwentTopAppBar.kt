package luke.koz.presentation.scaffold

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import luke.koz.presentation.scaffold.components.CoreTopAppBarTitle
import luke.koz.presentation.theme.GwentApiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GwentTopAppBar(
    onProfileClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {
    TopAppBar(
        title = {
            CoreTopAppBarTitle()
        },
        actions = {
            IconButton(onClick = onSearchClicked) {
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

@Preview
@Composable
private fun GwentTopAppBarPrev() {
    GwentApiTheme {
        GwentTopAppBar(
            onProfileClicked = { },
            onSearchClicked = { }
        )
    }
}