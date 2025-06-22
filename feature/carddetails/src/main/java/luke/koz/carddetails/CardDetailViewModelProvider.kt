package luke.koz.carddetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import luke.koz.di.AppDependencyProvider

@Composable
fun provideCardDetailViewModel(): CardDetailViewModel {
    val context = LocalContext.current
    val provider = context.applicationContext as AppDependencyProvider
    val factory = provider.getViewModelFactory(CardDetailViewModel::class.java)

    return viewModel(factory = factory)
}