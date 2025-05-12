package luke.koz.gwentapi.domain.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import luke.koz.gwentapi.data.repository.CardRepository

class CardGalleryViewModelFactory(private val repository: CardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CardGalleryViewModel(repository) as T
    }
}