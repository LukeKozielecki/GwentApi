package luke.koz.gwentapi.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import luke.koz.gwentapi.data.datasource.CardLocalDataSource
import luke.koz.gwentapi.data.datasource.CardRemoteDataSource
import luke.koz.gwentapi.data.local.database.AppDatabase
import luke.koz.gwentapi.data.local.database.GwentApplication
import luke.koz.gwentapi.data.remote.api.ApiClient
import luke.koz.gwentapi.data.repository.CardRepository
import luke.koz.gwentapi.domain.viewModel.CardState
import luke.koz.gwentapi.domain.viewModel.CardViewModel
import luke.koz.gwentapi.domain.viewModel.ViewModelFactory

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
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = card.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = card.flavor, style = MaterialTheme.typography.bodySmall)
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