package luke.koz.di

import android.content.Context
import coil3.ImageLoader
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import luke.koz.data.datasource.CardLocalDataSource
import luke.koz.data.datasource.CardLocalDataSourceImpl
import luke.koz.data.datasource.CardRemoteDataSource
import luke.koz.data.datasource.CardRemoteDataSourceImpl
import luke.koz.data.datasource.FirebaseUserLikesDataSource
import luke.koz.data.local.database.AppDatabase
import luke.koz.data.network.NetworkConnectivityCheckerImpl
import luke.koz.data.remote.api.ApiService
import luke.koz.data.repository.AuthRepositoryImpl
import luke.koz.data.repository.AuthStatusRepositoryImpl
import luke.koz.data.repository.CardDetailsRepositoryImpl
import luke.koz.data.repository.CardGalleryRepositoryImpl
import luke.koz.domain.NetworkConnectivityChecker
import luke.koz.domain.auth.usecase.LoginUseCase
import luke.koz.domain.auth.usecase.LoginUseCaseImpl
import luke.koz.domain.auth.usecase.LogoutUseCaseImpl
import luke.koz.domain.auth.usecase.RegisterUseCase
import luke.koz.domain.auth.usecase.RegisterUseCaseImpl
import luke.koz.domain.cardgallery.GetCardDetailUseCase
import luke.koz.domain.cardgallery.GetCardDetailUseCaseImpl
import luke.koz.domain.cardgallery.GetCardGalleryDataUseCase
import luke.koz.domain.cardgallery.GetCardGalleryDataUseCaseImpl
import luke.koz.domain.cardgallery.RefreshCardGalleryDataUseCase
import luke.koz.domain.cardgallery.RefreshCardGalleryDataUseCaseImpl
import luke.koz.domain.cardgallery.ToggleCardLikeUseCase
import luke.koz.domain.cardgallery.ToggleCardLikeUseCaseImpl
import luke.koz.domain.repository.CardDetailsRepository
import luke.koz.domain.repository.CardGalleryRepository
import luke.koz.domain.repository.UserLikesDataSource
import luke.koz.domain.repository.AuthRepository
import luke.koz.domain.repository.AuthStatusRepository
import luke.koz.infrastructure.PersistentImageLoader
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val FIREBASE_BASE_URL = "https://gwent-api-712bc-default-rtdb.europe-west1.firebasedatabase.app"
private const val GWENT_API_BASE_URL = "https://api.gwent.one/"

/**
 * Main application container for manual dependency injection.
 * This class is responsible for creating and providing all application-wide singletons
 * and their dependencies. It serves as the composition root for the application.
 */
class AppContainer(private val applicationContext: Context) {

    // --- Infrastructure Components (General Utilities) ---

    init {
        FirebaseApp.initializeApp(applicationContext)
    }

    val imageLoader: ImageLoader by lazy {
        PersistentImageLoader.create(applicationContext)
    }

    // --- Data Layer Components ---

    // Firebase related
    val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance(FIREBASE_BASE_URL)
    }

    val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val userLikesDataSource: UserLikesDataSource by lazy {
        FirebaseUserLikesDataSource(firebaseDatabase, firebaseAuth, networkConnectivityChecker)
    }

    val networkConnectivityChecker : NetworkConnectivityChecker by lazy {
        NetworkConnectivityCheckerImpl(applicationContext)
    }

    // Room Database related
    val appDatabase: AppDatabase by lazy {
        AppDatabase.getDatabase(applicationContext)
    }

    val cardDao = appDatabase.cardDao()

    val cardLocalDataSource: CardLocalDataSource by lazy {
        CardLocalDataSourceImpl(cardDao)
    }

    // Retrofit related
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(GWENT_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val cardRemoteDataSource: CardRemoteDataSource by lazy {
        CardRemoteDataSourceImpl(apiService)
    }

    val loginUseCase: LoginUseCase by lazy {
        LoginUseCaseImpl(authRepository)
    }

    val registerUseCase: RegisterUseCase by lazy {
        RegisterUseCaseImpl(authRepository)
    }

    val logoutUseCase : LogoutUseCaseImpl by lazy {
        LogoutUseCaseImpl(authRepository)
    }

    // --- Repository Implementation (Data Layer) ---

    val authStatusRepository: AuthStatusRepository by lazy {
        AuthStatusRepositoryImpl(firebaseAuth = firebaseAuth)
    }

    val cardGalleryRepository: CardGalleryRepository by lazy {
        CardGalleryRepositoryImpl(
            remote = cardRemoteDataSource,
            local = cardLocalDataSource,
            userLikesDataSource = userLikesDataSource,
            networkConnectivityChecker = networkConnectivityChecker,
            auth = firebaseAuth
        )
    }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl()
    }

    val cardDetailsRepository: CardDetailsRepository by lazy {
        CardDetailsRepositoryImpl(
            remote = cardRemoteDataSource,
            local = cardLocalDataSource
        )
    }

    // --- CardGalleryUseCases ---

    val getCardGalleryDataUseCase: GetCardGalleryDataUseCase by lazy {
        GetCardGalleryDataUseCaseImpl(
            cardGalleryRepository = cardGalleryRepository,
            userLikesDataSource = userLikesDataSource,
            authStatusRepository = authStatusRepository
        )
    }

    val refreshCardGalleryDataUseCase: RefreshCardGalleryDataUseCase by lazy {
        RefreshCardGalleryDataUseCaseImpl(
            cardGalleryRepository = cardGalleryRepository,
            userLikesDataSource = userLikesDataSource,
            authStatusRepository = authStatusRepository,
            networkConnectivityChecker = networkConnectivityChecker
        )
    }

    val toggleCardLikeUseCase: ToggleCardLikeUseCase by lazy {
        ToggleCardLikeUseCaseImpl(
            cardGalleryRepository = cardGalleryRepository
        )
    }

    val getCardDetailUseCase: GetCardDetailUseCase by lazy {
        GetCardDetailUseCaseImpl(
            cardGalleryRepository = cardDetailsRepository
        )
    }
}