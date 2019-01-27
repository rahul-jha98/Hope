package com.rahul.hope.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(EmergencyContactEntry::class), (ChatRoomEntry::class)], version = 1, exportSchema = false)
abstract class SheSafeDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "sheSafeDb"
        private val LOCK = Object()

        @Volatile
        var databaseInstance : SheSafeDatabase? = null

        fun getInstance(context: Context) : SheSafeDatabase {
            if(databaseInstance == null) {
                synchronized(LOCK) {
                    if(databaseInstance == null) {
                        databaseInstance = Room.databaseBuilder(context.applicationContext,
                            SheSafeDatabase::class.java,
                            SheSafeDatabase.DATABASE_NAME).build()
                    }
                }
            }
            return databaseInstance!!
        }
    }

    abstract fun emergencyContactsDao() : EmergencyContactsDao
    abstract fun chatRoomDao() : ChatRoomDao
}