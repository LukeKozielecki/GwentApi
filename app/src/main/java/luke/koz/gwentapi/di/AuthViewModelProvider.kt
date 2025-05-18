package luke.koz.gwentapi.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import luke.koz.gwentapi.data.repository.AuthRepositoryImpl
import luke.koz.gwentapi.ui.viewmodel.AuthViewModel
import luke.koz.gwentapi.ui.viewmodel.factory.AuthViewModelFactory

@Composable
fun provideAuthViewModel() : AuthViewModel {
    val repository = remember {
        AuthRepositoryImpl()
    }
    return viewModel(factory = AuthViewModelFactory(repository = repository))
}