package luke.koz.gwentapi.domain.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import luke.koz.gwentapi.data.repository.CardRepository
import luke.koz.gwentapi.domain.model.CardGalleryEntry

class CardViewModel (private val repository: CardRepository) : ViewModel (){
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
    fun getAllCards(){
        _cardState.value = CardState.Loading
        viewModelScope.launch {
            repository.getAllCards()
                .catch { e ->
                    _cardState.value = CardState.Error("Error: ${e.message}")
                }
                .collect { cards ->
                    _cardState.value = CardState.Success(cards)
                }
        }
    }
}

sealed class CardState {
    object Empty : CardState()
    object Loading : CardState()
    data class Success(val cards: List<CardGalleryEntry>) : CardState()
    data class Error(val message: String) : CardState()
}

class ViewModelFactory(private val repository: CardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CardViewModel(repository) as T
    }
}