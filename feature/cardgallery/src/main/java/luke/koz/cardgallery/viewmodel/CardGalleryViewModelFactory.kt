package luke.koz.cardgallery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import luke.koz.domain.NetworkConnectivityChecker
import luke.koz.domain.repository.AuthStatusRepository
import luke.koz.domain.repository.CardGalleryRepository
import luke.koz.domain.repository.UserLikesDataSource

class CardGalleryViewModelFactory(
    private val repository: CardGalleryRepository,
    private val userLikesDataSource: UserLikesDataSource,
    private val authStatusRepository: AuthStatusRepository,
    private val networkConnectivityChecker: NetworkConnectivityChecker
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardGalleryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardGalleryViewModel(repository, userLikesDataSource, authStatusRepository, networkConnectivityChecker) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}