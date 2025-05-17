package luke.koz.gwentapi.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import luke.koz.gwentapi.data.repository.CardGalleryRepository
import luke.koz.gwentapi.ui.viewmodel.CardGalleryViewModel

class CardGalleryViewModelFactory(private val repository: CardGalleryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CardGalleryViewModel(repository) as T
    }
}