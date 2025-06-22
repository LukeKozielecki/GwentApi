package luke.koz.gwentapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import luke.koz.gwentapi.navigation.NavigationHost
import luke.koz.presentation.theme.GwentApiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GwentApiTheme {
                Box(Modifier.fillMaxSize()) {
                    NavigationHost()
                }
            }
        }
    }
}