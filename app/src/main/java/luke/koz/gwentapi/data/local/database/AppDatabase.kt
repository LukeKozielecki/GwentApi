package luke.koz.gwentapi.data.local.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import luke.koz.gwentapi.MainActivity
import luke.koz.gwentapi.data.local.dao.CardDao
import luke.koz.gwentapi.data.local.entity.CardEntity
import luke.koz.gwentapi.data.remote.utils.PersistentImageLoader

@Database(
    entities = [CardEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gwent-db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}