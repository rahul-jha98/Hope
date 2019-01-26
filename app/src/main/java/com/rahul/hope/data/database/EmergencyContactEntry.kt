package com.rahul.hope.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency_contacts")
class EmergencyContactEntry (
    @PrimaryKey
    var phoneNo : String = "",
    var name : String = "Name not provided",
    var isStandard : Int = 0
)