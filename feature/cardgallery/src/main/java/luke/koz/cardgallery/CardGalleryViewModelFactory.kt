package luke.koz.cardgallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import luke.koz.cardgallery.viewmodel.CardGalleryViewModel
import luke.koz.domain.model.CardGalleryRepository
import luke.koz.domain.model.UserLikesDataSource

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