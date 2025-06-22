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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import luke.koz.search.provideSearchGalleryViewModel
import luke.koz.auth.AuthScreen
import luke.koz.carddetails.CardDetailScreen
import luke.koz.cardgallery.CardGalleryScreen
import luke.koz.gwentapi.application.GwentApplication
import luke.koz.navigation.AuthDestination
import luke.koz.navigation.CardDetailDestination
import luke.koz.navigation.CardGalleryDestination
import luke.koz.navigation.SearchDestination
import luke.koz.search.SearchScreen
import luke.koz.search.SearchViewModel

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val appDependencyProvider = context.applicationContext as GwentApplication
    val imageLoader = appDependencyProvider.getImageLoader()
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
                    navController = navController,
                    imageLoader = imageLoader
                )
            }
        }
        composable<CardDetailDestination> { backStackEntry ->
            val args = backStackEntry.toRoute<CardDetailDestination>()
            CardDetailScreen(
                cardId = args.cardId,
                onBack = { navController.popBackStack() },
                navController = navController,
                imageLoader = imageLoader
            )
        }

        composable<SearchDestination> {
            val searchViewModel: SearchViewModel =
                provideSearchGalleryViewModel()
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
                imageLoader = imageLoader,
                navController = navController
            )
        }
    }
}