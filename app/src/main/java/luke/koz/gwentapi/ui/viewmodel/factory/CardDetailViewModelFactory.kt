package luke.koz.gwentapi.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import luke.koz.gwentapi.data.repository.CardDetailsRepository
import luke.koz.gwentapi.ui.viewmodel.CardDetailViewModel

class CardDetailViewModelFactory(private val repository: CardDetailsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CardDetailViewModel(repository) as T
    }
}