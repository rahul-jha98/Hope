package com.rahul.hope.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EmergencyContactsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contact : EmergencyContactEntry)

    @Query("SELECT * FROM emergency_contacts WHERE isStandard = 1")
    fun getStandardContacts() : LiveData<List<EmergencyContactEntry>>

    @Query("SELECT * FROM emergency_contacts WHERE isStandard = 0")
    fun getPersonalContacts() : LiveData<List<EmergencyContactEntry>>
}