package luke.koz.gwentapi.application

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil3.ImageLoader
import luke.koz.auth.viewmodel.AuthViewModel
import luke.koz.auth.viewmodel.AuthViewModelFactory
import luke.koz.carddetails.viewmodel.CardDetailViewModel
import luke.koz.carddetails.viewmodel.CardDetailViewModelFactory
import luke.koz.cardgallery.viewmodel.CardGalleryViewModelFactory
import luke.koz.cardgallery.viewmodel.CardGalleryViewModel
import luke.koz.di.AppDependencyProvider
import luke.koz.search.viewmodel.SearchViewModel
import luke.koz.search.viewmodel.SearchViewModelFactory

class GwentApplication : Application(), AppDependencyProvider {
    override fun <T : ViewModel> getViewModelFactory(clazz: Class<T>): ViewModelProvider.Factory {
        return when {
            clazz.isAssignableFrom(SearchViewModel::class.java) ->
                SearchViewModelFactory(appContainer.searchCardsUseCase)
            clazz.isAssignableFrom(CardDetailViewModel::class.java) ->
                CardDetailViewModelFactory(appContainer.getCardDetailUseCase)
            clazz.isAssignableFrom(CardGalleryViewModel::class.java) ->
                CardGalleryViewModelFactory(
                    getCardGalleryDataUseCase = appContainer.getCardGalleryDataUseCase,
                    refreshCardGalleryDataUseCase = appContainer.refreshCardGalleryDataUseCase,
                    toggleCardLikeUseCase = appContainer.toggleCardLikeUseCase,
                    authStatusRepository = appContainer.authStatusRepository,
                    networkConnectivityChecker = appContainer.networkConnectivityChecker
                )
            clazz.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModelFactory(
                    appContainer.authRepository,
                    appContainer.loginUseCase,
                    appContainer.registerUseCase,
                    appContainer.logoutUseCase
                )
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