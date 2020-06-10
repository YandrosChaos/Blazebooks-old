package com.blazebooks.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.blazebooks.data.db.entities.StoredBook

/**
 * Singleton for LocalStorageDatabase.
 *
 * @see StoredBookDAO
 *
 * @author Victor Gonzalez
 */
@Database(entities = [StoredBook::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getStoredBookDAO(): StoredBookDAO


    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "blacebooks_local_storage.db"
            ).build()
    }
}