package com.blazebooks.control.localStorage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.blazebooks.control.localStorage.dao.FavBookDAO
import com.blazebooks.control.localStorage.dao.StoredBookDAO
import com.blazebooks.control.localStorage.model.FavBook
import com.blazebooks.control.localStorage.model.StoredBook

/**
 * Singleton for LocalStorageDatabase.
 *
 * @see FavBookDAO
 * @see StoredBookDAO
 *
 * @author Victor Gonzalez
 */
@Database(entities = [FavBook::class, StoredBook::class], version = 1)
abstract class LocalStorageSingleton : RoomDatabase() {
    abstract fun favBookDAO(): FavBookDAO
    abstract fun storedBookDAO(): StoredBookDAO


    companion object {
        private lateinit var context: Context
        private val DATABASE_SINGLETON: LocalStorageSingleton by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(
                context,
                LocalStorageSingleton::class.java,
                "blacebooks_local_storage.db"
            ).allowMainThreadQueries().build()
        }

        fun getDatabase(context: Context): LocalStorageSingleton {
            Companion.context = context.applicationContext
            return DATABASE_SINGLETON
        }
    }
}