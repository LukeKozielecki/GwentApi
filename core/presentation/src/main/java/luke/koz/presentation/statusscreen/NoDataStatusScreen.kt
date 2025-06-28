package luke.koz.presentation.statusscreen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import luke.koz.presentation.R
import luke.koz.presentation.theme.GwentApiTheme

@Composable
fun NoDataStatusScreen(emptyStateDescription: String, toastMessage: String?) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .then(
                    if (toastMessage != null) {
                        Modifier.clickable { showHelpfulToast(context, toastMessage = toastMessage) }
                    } else {
                        Modifier
                    }
                )                .padding(horizontal = 8.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.gwent_one_api_app_icon_96x96),
                contentDescription = null,
                modifier = Modifier.size(64.dp).align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = emptyStateDescription,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

private fun showHelpfulToast(context: Context, toastMessage: String) : Unit{
    Toast.makeText(
        context,
        toastMessage,
        Toast.LENGTH_LONG
    ).show()
}

@Preview (showBackground = true)
@Composable
private fun NoDataStatusScreenPrev() {
    GwentApiTheme {
        NoDataStatusScreen(
            emptyStateDescription = "No cards found",
            toastMessage = "Please try checking internet connection"
        )
    }
}