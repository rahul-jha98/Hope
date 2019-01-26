package com.rahul.hope.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.rahul.hope.AppExecutors
import com.rahul.hope.data.database.EmergencyContactEntry
import com.rahul.hope.data.database.EmergencyContactsDao


class DataRepository(private var mContactsDao: EmergencyContactsDao,
                     private var mExecutors: AppExecutors
) {
    companion object {
        private val LOG_TAG = DataRepository::class.java.simpleName

        private val LOCK = Object()
        private var sInstance : DataRepository? = null
        private var mInitialized = false

        @Synchronized
        fun getInstance( mEmergencyContactsDao: EmergencyContactsDao,
                        executors: AppExecutors) : DataRepository{
            Log.d(LOG_TAG, "Getting the repository")
            if(sInstance == null) {
                synchronized(LOCK) {
                    sInstance = DataRepository(mEmergencyContactsDao, executors)
                    Log.d(LOG_TAG, "Made a new repository")
                }
            }
            return sInstance!!
        }
    }

    private fun init() {
        addContact(EmergencyContactEntry( "112", "All In One Helpline", 1))
        addContact(EmergencyContactEntry( "181", "Women's Helpline", 1))
    }

    @Synchronized
    fun addContact(contact : EmergencyContactEntry) {
        mExecutors.diskIO().execute {
            mContactsDao.insertContact(contact)
        }
        Log.d(LOG_TAG, contact.name)
    }

    fun getStandardContacts() : LiveData<List<EmergencyContactEntry>> {
        return mContactsDao.getStandardContacts()
    }

    fun getPersonalContacts() : LiveData<List<EmergencyContactEntry>> {
        return mContactsDao.getPersonalContacts()
    }
}