package luke.koz.carddetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import luke.koz.carddetails.components.CardDetailsStateHandler
import luke.koz.carddetails.viewmodel.CardDetailViewModel
import luke.koz.carddetails.viewmodel.provideCardDetailViewModel
import luke.koz.presentation.scaffold.CoreTopAppBar
import luke.koz.presentation.state.CardDetailsState

@Composable
fun CardDetailScreen(
    cardId : Int,
    onCardClick : (Int, String) -> Unit,
    onBack : () -> Unit,
    imageLoader: ImageLoader
) {
    val viewModel: CardDetailViewModel = provideCardDetailViewModel()
    val cardState by viewModel.cardState
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = cardId) {
        if (cardState == CardDetailsState.Empty) {
            viewModel.getCardById(cardId)
        }
    }

    Scaffold(
        topBar = {
            CoreTopAppBar()
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                CardDetailsStateHandler(
                    state = cardState,
                    onCardClick = onCardClick,
                    imageLoader = imageLoader
                )
            }
        }
    }
}