package com.amazinline.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amazinline.data.model.Customer

/**
 * The Room Database that contains the customer table.
 */
@Database(entities = [Customer::class], version = 2, exportSchema = false)
abstract class ItemDb : RoomDatabase() {
    abstract fun customerDao(): CustomerDao

    companion object {
        private var INSTANCE: ItemDb? = null

        private val lock = Any()

        @JvmStatic
        fun getInstance(context: Context): ItemDb {

            // When calling this instance concurrently from multiple threads we might end up loosing data
            // So we use this method, 'synchronized' to lock the instance
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, ItemDb::class.java, "items.db")
                        .fallbackToDestructiveMigration()  // temporary
                        .build()
                }
                return INSTANCE!!
            }
        }
    }
}