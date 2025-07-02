package luke.koz.gwentapi.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import luke.koz.auth.AuthScreen
import luke.koz.cardartshowcase.ArtShowCaseScreen
import luke.koz.carddetails.CardDetailScreen
import luke.koz.cardgallery.CardGalleryScreen
import luke.koz.gwentapi.application.GwentApplication
import luke.koz.navigation.AuthDestination
import luke.koz.navigation.CardArtShowcaseDestination
import luke.koz.navigation.CardDetailDestination
import luke.koz.navigation.CardGalleryDestination
import luke.koz.navigation.SearchDestination
import luke.koz.search.SearchScreen
import luke.koz.search.viewmodel.SearchViewModel
import luke.koz.search.viewmodel.provideSearchGalleryViewModel

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
                }
            )
        }
        composable<CardGalleryDestination> {
            CardGalleryScreen(
                onCardClick = { cardId ->
                    navController.navigate(CardDetailDestination(cardId))
                },
                onProfileClicked = { navController.navigate(AuthDestination) },
                onSearchClicked = { navController.navigate(SearchDestination) },
                imageLoader = imageLoader
            )
        }
        composable<CardDetailDestination> { backStackEntry ->
            val args = backStackEntry.toRoute<CardDetailDestination>()
            CardDetailScreen(
                cardId = args.cardId,
                onBack = { navController.popBackStack() },
                onCardClick = { artId, cardColor ->
                    navController.navigate(CardArtShowcaseDestination(artId, cardColor)) },
                imageLoader = imageLoader
            )
        }

        composable<SearchDestination> {
            val searchViewModel: SearchViewModel =
                provideSearchGalleryViewModel()
            SearchScreen(
                viewModel = searchViewModel,
                onCardClick = { cardId ->
                    navController.navigate(CardDetailDestination(cardId))
                },
                onPopBackStack = { navController.popBackStack() },
                imageLoader = imageLoader,
            )
        }

        composable<CardArtShowcaseDestination> { backStackEntry ->
            val args = backStackEntry.toRoute<CardArtShowcaseDestination>()
            ArtShowCaseScreen(
                cardArtId = args.cardArtId,
                cardColor = args.cardColor,
                imageLoader = imageLoader
            )
        }
    }
}