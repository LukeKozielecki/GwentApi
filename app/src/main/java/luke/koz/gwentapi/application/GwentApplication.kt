package luke.koz.gwentapi.application

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import luke.koz.gwentapi.data.datasource.FirebaseUserLikesDataSource
import luke.koz.gwentapi.data.datasource.UserLikesDataSource
import luke.koz.gwentapi.data.local.database.AppDatabase
import luke.koz.gwentapi.data.remote.utils.PersistentImageLoader

private const val FIREBASE_BASE_URL = "https://gwent-api-712bc-default-rtdb.europe-west1.firebasedatabase.app"

class GwentApplication : Application() {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }
    val persistentImageLoader by lazy {
        PersistentImageLoader.create(this)
    }
    lateinit var firebaseDatabase: FirebaseDatabase
        private set

    lateinit var firebaseAuth: FirebaseAuth
        private set

    lateinit var userLikesDataSource: UserLikesDataSource
        private set

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_BASE_URL)
        firebaseAuth = FirebaseAuth.getInstance()
        userLikesDataSource = FirebaseUserLikesDataSource(firebaseDatabase, firebaseAuth)
    }
}