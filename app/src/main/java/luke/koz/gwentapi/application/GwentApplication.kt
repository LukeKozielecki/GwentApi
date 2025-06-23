package luke.koz.gwentapi.application

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil3.ImageLoader
import luke.koz.auth.AuthViewModel
import luke.koz.auth.AuthViewModelFactory
import luke.koz.carddetails.viewmodel.CardDetailViewModel
import luke.koz.carddetails.viewmodel.CardDetailViewModelFactory
import luke.koz.cardgallery.viewmodel.CardGalleryViewModelFactory
import luke.koz.cardgallery.viewmodel.CardGalleryViewModel
import luke.koz.di.AppContainer
import luke.koz.di.AppDependencyProvider
import luke.koz.search.SearchViewModel
import luke.koz.search.SearchViewModelFactory

class GwentApplication : Application(), AppDependencyProvider {
    override fun <T : ViewModel> getViewModelFactory(clazz: Class<T>): ViewModelProvider.Factory {
        return when {
            clazz.isAssignableFrom(SearchViewModel::class.java) ->
                SearchViewModelFactory(appContainer.cardGalleryRepository)
            clazz.isAssignableFrom(CardDetailViewModel::class.java) ->
                CardDetailViewModelFactory(appContainer.cardDetailsRepository)
            clazz.isAssignableFrom(CardGalleryViewModel::class.java) ->
                CardGalleryViewModelFactory(
                    repository = appContainer.cardGalleryRepository,
                    userLikesDataSource = appContainer.userLikesDataSource,
                    auth = appContainer.firebaseAuth
                )
            clazz.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModelFactory(appContainer.authRepository)
            // todo add other ViewModel factories
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    override fun getImageLoader(): ImageLoader {
        return appContainer.imageLoader
    }

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }

}