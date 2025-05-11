package luke.koz.gwentapi.application

import android.app.Application
import luke.koz.gwentapi.data.local.database.AppDatabase
import luke.koz.gwentapi.data.remote.utils.PersistentImageLoader

class GwentApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val persistentImageLoader by lazy {
        PersistentImageLoader.create(this)
    }
}