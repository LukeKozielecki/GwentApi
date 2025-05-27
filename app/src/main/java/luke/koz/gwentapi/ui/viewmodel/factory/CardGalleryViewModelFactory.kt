package luke.koz.gwentapi.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import luke.koz.gwentapi.data.datasource.UserLikesDataSource
import luke.koz.gwentapi.data.repository.CardGalleryRepository
import luke.koz.gwentapi.ui.viewmodel.CardGalleryViewModel

class CardGalleryViewModelFactory(
    private val repository: CardGalleryRepository,
    private val userLikesDataSource: UserLikesDataSource,
    private val auth: FirebaseAuth
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CardGalleryViewModel(repository, userLikesDataSource, auth) as T
    }
}