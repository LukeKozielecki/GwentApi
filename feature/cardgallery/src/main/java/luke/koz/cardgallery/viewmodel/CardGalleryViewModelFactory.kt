package luke.koz.cardgallery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import luke.koz.domain.NetworkConnectivityChecker
import luke.koz.domain.cardgallery.GetCardGalleryDataUseCase
import luke.koz.domain.cardgallery.RefreshCardGalleryDataUseCase
import luke.koz.domain.cardgallery.ToggleCardLikeUseCase
import luke.koz.domain.repository.AuthStatusRepository

class CardGalleryViewModelFactory(
    private val getCardGalleryDataUseCase: GetCardGalleryDataUseCase,
    private val refreshCardGalleryDataUseCase: RefreshCardGalleryDataUseCase,
    private val toggleCardLikeUseCase: ToggleCardLikeUseCase,
    private val authStatusRepository: AuthStatusRepository,
    private val networkConnectivityChecker: NetworkConnectivityChecker
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardGalleryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardGalleryViewModel(
                getCardGalleryDataUseCase = getCardGalleryDataUseCase,
                refreshCardGalleryDataUseCase = refreshCardGalleryDataUseCase,
                toggleCardLikeUseCase = toggleCardLikeUseCase,
                authStatusRepository = authStatusRepository,
                networkConnectivityChecker = networkConnectivityChecker
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}