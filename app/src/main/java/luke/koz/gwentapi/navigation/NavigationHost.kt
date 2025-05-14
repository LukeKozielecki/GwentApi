package luke.koz.gwentapi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import luke.koz.gwentapi.ui.carddetailsscreen.CardDetailScreen
import luke.koz.gwentapi.ui.cardgalleryscreen.CardGalleryScreen

@Serializable
object CardGalleryDestination

@Serializable
data class CardDetailDestination(val cardId: Int)

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = CardGalleryDestination,
        modifier = modifier
    ) {
        composable<CardGalleryDestination> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { navController.navigate(CardDetailDestination((201729))) }) {
                    Text("View Card Details")
                }
                CardGalleryScreen(
                    cardId = 201729,
                    onCardClick = { cardId ->
                        navController.navigate(CardDetailDestination(cardId))
                    }
                )
            }
        }
        composable<CardDetailDestination> { backStackEntry ->
            val args = backStackEntry.toRoute<CardDetailDestination>()
            CardDetailScreen(
                cardId = args.cardId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}