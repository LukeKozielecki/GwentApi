package luke.koz.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import luke.koz.data.local.dao.CardDao
import luke.koz.data.local.entity.CardEntity

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