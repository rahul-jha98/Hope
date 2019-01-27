package com.rahul.hope.data.database
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chatrooms")
class ChatRoomEntry(
    @PrimaryKey
    var chatRoomName : String
)