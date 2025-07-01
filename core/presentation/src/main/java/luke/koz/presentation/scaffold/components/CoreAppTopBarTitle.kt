package luke.koz.presentation.scaffold.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import luke.koz.presentation.R
import luke.koz.presentation.scaffold.CoreTopAppBar
import luke.koz.presentation.theme.GwentApiTheme

/**
 *  A composable that represents the application's consistent primary title content
 *  for use within the `title` slot of a [CoreTopAppBar].
 *
 *  It displays the application's logo ([R.drawable.gwent_one_api_app_icon_96x96]) alongside
 *  the static text "GwentApi Compendium",
 */
@Composable
fun CoreTopAppBarTitle() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.gwent_one_api_app_icon_96x96),
            contentDescription = "App Logo",
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = "GwentApi Compendium",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview
@Composable
private fun CoreTopAppBarTitlePrev() {
    GwentApiTheme {
        CoreTopAppBarTitle()
    }
}