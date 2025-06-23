package luke.koz.carddetails.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import luke.koz.domain.model.CardDetailsRepository

class CardDetailViewModel (private val repository: CardDetailsRepository) : ViewModel(){
    private val _cardState = mutableStateOf<luke.koz.presentation.CardDetailsState>(luke.koz.presentation.CardDetailsState.Empty)
    val cardState: State<luke.koz.presentation.CardDetailsState> = _cardState

    fun getCardById(cardId: Int) {
        _cardState.value = luke.koz.presentation.CardDetailsState.Loading
        viewModelScope.launch {
            repository.getCardDetails(cardId)
                .catch { e ->
                    _cardState.value = luke.koz.presentation.CardDetailsState.Error("Error: ${e.message}")
                }
                .collect { card ->
                    _cardState.value = luke.koz.presentation.CardDetailsState.Success(listOf(card))
                }
        }
    }
}