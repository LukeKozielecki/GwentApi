package luke.koz.carddetails.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import luke.koz.domain.cardgallery.GetCardDetailUseCase
import luke.koz.presentation.CardDetailsState

class CardDetailViewModel (private val getCardDetailUseCase: GetCardDetailUseCase) : ViewModel(){
    private val _cardState = mutableStateOf<CardDetailsState>(CardDetailsState.Empty)
    val cardState: State<CardDetailsState> = _cardState

    fun getCardById(cardId: Int) {
        _cardState.value = CardDetailsState.Loading
        viewModelScope.launch {
            getCardDetailUseCase(cardId)
                .catch { e ->
                    _cardState.value = CardDetailsState.Error("Error: ${e.message}")
                }
                .collect { card ->
                    _cardState.value = CardDetailsState.Success(listOf(card))
                }
        }
    }
}