package luke.koz.gwentapi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import luke.koz.gwentapi.R
import luke.koz.gwentapi.data.datasource.CardLocalDataSource
import luke.koz.gwentapi.data.datasource.CardRemoteDataSource
import luke.koz.gwentapi.data.local.database.AppDatabase
import luke.koz.gwentapi.data.local.database.GwentApplication
import luke.koz.gwentapi.data.remote.api.ApiClient
import luke.koz.gwentapi.data.repository.CardRepository
import luke.koz.gwentapi.domain.viewModel.CardState
import luke.koz.gwentapi.domain.viewModel.CardViewModel
import luke.koz.gwentapi.domain.viewModel.ViewModelFactory
import luke.koz.gwentapi.ui.theme.GwentApiTheme

@Composable
fun CardEntryHome(cardId: Int) {
    val context = LocalContext.current
    val application = context.applicationContext as GwentApplication
    val database = application.database
    val apiService = remember { ApiClient.apiService }
    val repository = remember {
        CardRepository(
            remote = CardRemoteDataSource(apiService),
            local = CardLocalDataSource(database.cardDao())
        )
    }
    val viewModel: CardViewModel = viewModel(
        factory = ViewModelFactory(repository)
    )
    val cardState by viewModel.cardState

    LaunchedEffect(key1 = cardId) {
        viewModel.getAllCards()
    }

    when (val state = cardState) {
        is CardState.Empty -> {
            // Initial state
        }
        is CardState.Loading -> {
            CircularProgressIndicator()
        }
        is CardState.Success -> {
            // Print mock card details to composable
            if (state.cards.isNotEmpty()) {
                LazyColumn {
                    items(state.cards) {card ->
                        Row {
                            CoilImage(card.artId)
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(text = card.name, style = MaterialTheme.typography.titleMedium)
                                Text(text = card.flavor, style = MaterialTheme.typography.bodySmall)
                                Text(text = card.artId.toString(), style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        HorizontalDivider()
                    }
                }
                Column {
                    Text(text = state.cards[0].name)
                    Text(text = state.cards[0].flavor)
                }
            } else {
                Text("No card found", color = Color.Red)
            }
        }
        is CardState.Error -> {
            Text(text = "Error: ${state.message}", color = Color.Red)
        }
    }
}

@Composable
fun CoilImage(cardId: Int) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://gwent.one/image/gwent/assets/card/art/low/${cardId}.jpg")
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.size(150.dp, 215.dp),
    )
}

@Preview
@Composable
private fun CoilImagePrev() {
    GwentApiTheme {
        CoilImage(1636)
    }
}