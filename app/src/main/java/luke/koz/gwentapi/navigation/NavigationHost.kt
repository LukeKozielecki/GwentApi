package luke.koz.gwentapi.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import luke.koz.gwentapi.di.provideSearchGalleryViewModel
import luke.koz.gwentapi.ui.screen.auth.AuthScreen
import luke.koz.gwentapi.ui.screen.carddetails.CardDetailScreen
import luke.koz.gwentapi.ui.screen.cardgallery.CardGalleryScreen
import luke.koz.gwentapi.ui.screen.search.SearchScreen
import luke.koz.gwentapi.ui.viewmodel.SearchViewModel

@Serializable
object CardGalleryDestination

@Serializable
data class CardDetailDestination(val cardId: Int)

@Serializable
object SearchDestination

@Serializable
object AuthDestination

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = CardGalleryDestination,
        enterTransition = { fadeIn(animationSpec = tween(200)) },
        exitTransition = { fadeOut(animationSpec = tween(200, delayMillis = 30)) },
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable<AuthDestination> {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(CardGalleryDestination) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                navController = navController,
                modifier = Modifier.fillMaxSize()
            )
        }
        composable<CardGalleryDestination> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CardGalleryScreen(
//                    cardId = 201729,
                    onCardClick = { cardId ->
                        navController.navigate(CardDetailDestination(cardId))
                    },
                    navController = navController
                )
            }
        }
        composable<CardDetailDestination> { backStackEntry ->
            val args = backStackEntry.toRoute<CardDetailDestination>()
            CardDetailScreen(
                cardId = args.cardId,
                onBack = { navController.popBackStack() },
                navController = navController
            )
        }

        composable<SearchDestination> {
            val searchViewModel: SearchViewModel = provideSearchGalleryViewModel()
            val searchState by searchViewModel.searchState
            SearchScreen(
                query = searchViewModel.query,
                updateQuery = { searchViewModel.updateQuery(it) },
                getCardByQuery = { searchViewModel.getCardByQuery() },
                searchState = searchState,
                onCardClick = { cardId ->
                    navController.navigate(CardDetailDestination(cardId))
                },
                closeSearch = { navController.popBackStack() },
                navController = navController
            )
        }
    }
}