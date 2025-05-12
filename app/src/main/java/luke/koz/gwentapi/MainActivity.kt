package luke.koz.gwentapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import luke.koz.gwentapi.ui.components.GwentTopAppBar
import luke.koz.gwentapi.ui.cardgalleryscreen.CardGalleryScreen
import luke.koz.gwentapi.ui.theme.GwentApiTheme

//TODO:
// - create scaffold topappbar. give it its own file
// - need to implement topappbar. need logo on the left, perhaps name of app to the right
//   and perhaps fancy ALL-THREE app feats to the left of icon. and to the left search icon
//   that fills bar with search bar when clicked and to the right user icon for loggin in.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GwentApiTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        GwentTopAppBar(
                            onProfileClicked = { /* TODO: Handle profile/login */ }
                        )
                    }
                ) { innerPadding ->
                    Column (modifier = Modifier.padding(innerPadding)) {
                        CardGalleryScreen(cardId = 201729)
                    }
                }
            }
        }
    }
}