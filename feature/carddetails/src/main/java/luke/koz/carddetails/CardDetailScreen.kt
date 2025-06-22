package luke.koz.carddetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import coil3.ImageLoader
import luke.koz.presentation.scaffold.DefaultScaffold
import luke.koz.presentation.scaffold.components.ScaffoldWrapper

@Composable
fun CardDetailScreen(
    cardId : Int,
    onBack : () -> Unit,
    navController: NavHostController,
    imageLoader: ImageLoader,
    scaffold: @Composable (NavHostController, @Composable (PaddingValues) -> Unit) -> Unit = { nav, content ->
        DefaultScaffold(navController = nav, content = content)
    }
) {
    val viewModel: CardDetailViewModel = provideCardDetailViewModel()
    val cardState by viewModel.cardState
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = cardId) {
        if (cardState == luke.koz.presentation.CardDetailsState.Empty) {
            viewModel.getCardById(cardId)
        }
    }

    ScaffoldWrapper(
        navController = navController,
        scaffold = scaffold
    ) {
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            CardDetailsStateHandler(
                state = cardState,
                onCardClick = { },
                imageLoader = imageLoader
            )
        }
    }
}