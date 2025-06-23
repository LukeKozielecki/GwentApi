package luke.koz.cardgallery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import luke.koz.domain.repository.CardGalleryRepository
import luke.koz.domain.repository.UserLikesDataSource

class CardGalleryViewModelFactory(
    private val repository: CardGalleryRepository,
    private val userLikesDataSource: UserLikesDataSource,
    private val auth: FirebaseAuth
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardGalleryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardGalleryViewModel(repository, userLikesDataSource, auth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}