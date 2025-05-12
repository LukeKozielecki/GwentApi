package luke.koz.gwentapi.ui.cardgalleryscreen.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import luke.koz.gwentapi.application.GwentApplication
import luke.koz.gwentapi.data.datasource.CardLocalDataSource
import luke.koz.gwentapi.data.datasource.CardRemoteDataSource
import luke.koz.gwentapi.data.remote.api.ApiClient
import luke.koz.gwentapi.data.repository.CardRepository
import luke.koz.gwentapi.domain.viewModel.SearchViewModel
import luke.koz.gwentapi.domain.viewModel.SearchViewModelFactory

@Composable
fun provideSearchGalleryViewModel(): SearchViewModel {
    val context = LocalContext.current
    val application = context.applicationContext as GwentApplication
    val repository = remember {
        CardRepository(
            remote = CardRemoteDataSource(ApiClient.apiService),
            local = CardLocalDataSource(application.database.cardDao())
        )
    }
    return viewModel(factory = SearchViewModelFactory(repository))
}