package luke.koz.presentation.scaffold

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import luke.koz.presentation.R
import luke.koz.presentation.theme.GwentApiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GwentTopAppBar(
    onProfileClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {
    TopAppBar(
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