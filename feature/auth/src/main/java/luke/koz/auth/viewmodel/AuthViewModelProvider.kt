package luke.koz.auth.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import luke.koz.di.AppDependencyProvider

@Composable
fun provideAuthViewModel() : AuthViewModel {
    val context = LocalContext.current
    val provider = context.applicationContext as AppDependencyProvider
    val factory = provider.getViewModelFactory(AuthViewModel::class.java)
    return viewModel(factory = factory)
}