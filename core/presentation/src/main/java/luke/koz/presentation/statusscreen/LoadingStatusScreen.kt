package luke.koz.presentation.statusscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import luke.koz.presentation.R
import luke.koz.presentation.theme.GwentApiTheme

@Composable
fun LoadingStatusScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Image(painter = painterResource(R.drawable.gwent_one_api_app_icon_96x96), contentDescription = null)
        CircularProgressIndicator(Modifier.size(64.dp))
    }
}

@Preview
@Composable
private fun LoadingStatusScreenPrev() {
    GwentApiTheme {
        LoadingStatusScreen()
    }
}