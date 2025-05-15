package luke.koz.gwentapi.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import luke.koz.gwentapi.data.repository.CardRepository
import luke.koz.gwentapi.ui.viewmodel.CardGalleryViewModel

class CardGalleryViewModelFactory(private val repository: CardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CardGalleryViewModel(repository) as T
    }
}