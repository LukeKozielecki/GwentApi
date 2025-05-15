package luke.koz.gwentapi.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import luke.koz.gwentapi.domain.viewModel.SearchViewModel
import luke.koz.gwentapi.ui.carddetailsscreen.CardDetailScreen
import luke.koz.gwentapi.ui.cardgalleryscreen.CardGalleryScreen
import luke.koz.gwentapi.ui.cardgalleryscreen.di.provideSearchGalleryViewModel
import luke.koz.gwentapi.ui.components.SearchScreen

@Serializable
object CardGalleryDestination

@Serializable
data class CardDetailDestination(val cardId: Int)

@Serializable
object SearchDestination

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = CardGalleryDestination,
        enterTransition = { fadeIn(animationSpec = tween(200)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) },
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
                closeSearch = { navController.popBackStack() }
            )
        }
    }
}