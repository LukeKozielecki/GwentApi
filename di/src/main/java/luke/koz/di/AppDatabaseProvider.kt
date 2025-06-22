package luke.koz.di

import android.content.Context
import luke.koz.data.local.database.AppDatabase

/**
 * Provides a singleton instance of AppDatabase.
 * This factory belongs to the DI layer, as it knows about the concrete AppDatabase implementation
 * and the Android Context required for its creation.
 */
object AppDatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = AppDatabase.getDatabase(context)
            INSTANCE = instance
            instance
        }
    }
}