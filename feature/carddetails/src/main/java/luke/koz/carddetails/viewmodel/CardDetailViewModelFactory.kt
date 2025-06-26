package luke.koz.carddetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import luke.koz.domain.cardgallery.GetCardDetailUseCase
import luke.koz.domain.repository.CardDetailsRepository

class CardDetailViewModelFactory(
    private val cardDetailUseCase: GetCardDetailUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardDetailViewModel(
                getCardDetailUseCase = cardDetailUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}