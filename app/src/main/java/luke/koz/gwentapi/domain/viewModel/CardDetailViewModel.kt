package luke.koz.gwentapi.domain.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import luke.koz.gwentapi.data.repository.CardRepository
import luke.koz.gwentapi.domain.state.CardState

class CardDetailViewModel (private val repository: CardRepository) : ViewModel(){
    private val _cardState = mutableStateOf<CardState>(CardState.Empty)
    val cardState: State<CardState> = _cardState

    fun getCardById(cardId: Int) {
        _cardState.value = CardState.Loading
        viewModelScope.launch {
            repository.getCard(cardId)
                .catch { e ->
                    _cardState.value = CardState.Error("Error: ${e.message}")
                }
                .collect { card ->
                    _cardState.value = CardState.Success(listOf(card))
                }
        }
    }
}