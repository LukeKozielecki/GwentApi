package luke.koz.gwentapi.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import luke.koz.gwentapi.application.GwentApplication
import luke.koz.gwentapi.data.datasource.CardLocalDataSource
import luke.koz.gwentapi.data.datasource.CardRemoteDataSource
import luke.koz.gwentapi.data.remote.api.ApiClient
import luke.koz.gwentapi.data.repository.CardGalleryRepository
import luke.koz.gwentapi.ui.viewmodel.SearchViewModel
import luke.koz.gwentapi.ui.viewmodel.factory.SearchViewModelFactory

@Composable
fun provideSearchGalleryViewModel(): SearchViewModel {
    val context = LocalContext.current
    val application = context.applicationContext as GwentApplication

    val firebaseUserLikesDataSource = remember {
        application.userLikesDataSource
    }
    val firebaseAuthInstance = remember {
        application.firebaseAuth
    }

    val repository = remember {
        CardGalleryRepository(
            remote = CardRemoteDataSource(ApiClient.apiService),
            local = CardLocalDataSource(application.database.cardDao()),
            userLikesDataSource = firebaseUserLikesDataSource,
            auth = firebaseAuthInstance
        )
    }
    return viewModel(factory = SearchViewModelFactory(repository))
}