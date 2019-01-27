package com.rahul.hope.viewmodels

import androidx.lifecycle.ViewModel
import com.rahul.hope.data.DataRepository

class ChatRoomViewModel(repository: DataRepository) : ViewModel() {
    var allChatRooms = repository.getChatRooms()
}