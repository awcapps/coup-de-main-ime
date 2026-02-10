package bkh.apps.coupdemain.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Base de données Room pour Coup de Main
 */
@Database(
    entities = [ChessGame::class, ChessMove::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ChessDatabase : RoomDatabase() {
    
    abstract fun gameDao(): GameDao
    
    companion object {
        @Volatile
        private var INSTANCE: ChessDatabase? = null
        
        /**
         * Singleton pattern pour éviter plusieurs instances
         */
        fun getDatabase(context: Context): ChessDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChessDatabase::class.java,
                    "coup_de_main_database"
                )
                .fallbackToDestructiveMigration() // Pour le développement
                .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}
