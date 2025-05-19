package luke.koz.gwentapi.ui.screen.carddetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import luke.koz.gwentapi.ui.state.CardDetailsState
import luke.koz.gwentapi.ui.viewmodel.CardDetailViewModel
import luke.koz.gwentapi.di.provideCardDetailViewModel
import luke.koz.gwentapi.ui.scaffold.DefaultScaffold
import luke.koz.gwentapi.ui.scaffold.components.ScaffoldWrapper

@Composable
fun CardDetailScreen(
    cardId : Int,
    onBack : () -> Unit,
    navController: NavHostController,
    scaffold: @Composable (NavHostController, @Composable (PaddingValues) -> Unit) -> Unit = { nav, content ->
        DefaultScaffold(navController = nav, content = content)
    }
) {
    val viewModel: CardDetailViewModel = provideCardDetailViewModel()
    val cardState by viewModel.cardState
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = cardId) {
        if (cardState == CardDetailsState.Empty) {
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
                onCardClick = {  }
            )
        }
    }
}