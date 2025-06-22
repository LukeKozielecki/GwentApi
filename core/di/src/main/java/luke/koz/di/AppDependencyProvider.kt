package luke.koz.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil3.ImageLoader

interface AppDependencyProvider {
    fun <T : ViewModel> getViewModelFactory(clazz: Class<T>): ViewModelProvider.Factory
    fun getImageLoader(): ImageLoader
}