package luke.koz.gwentapi.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import luke.koz.gwentapi.data.repository.CardRepository
import luke.koz.gwentapi.ui.state.CardState

class CardGalleryViewModel (private val repository: CardRepository) : ViewModel (){
    private val _cardState = mutableStateOf<CardState>(CardState.Empty)
    val cardState: State<CardState> = _cardState

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