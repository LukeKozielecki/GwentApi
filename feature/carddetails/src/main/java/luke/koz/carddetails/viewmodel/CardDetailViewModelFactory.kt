package luke.koz.carddetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import luke.koz.domain.repository.CardDetailsRepository

class CardDetailViewModelFactory(
    private val repository: CardDetailsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}