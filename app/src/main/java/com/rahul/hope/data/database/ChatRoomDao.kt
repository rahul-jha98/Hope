package com.rahul.hope.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChatRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChatRoom(chatRoom : ChatRoomEntry)

    @Query("SELECT * FROM chatrooms")
    fun getAllChatRooms() : LiveData<List<ChatRoomEntry>>
}