package com.rahul.hope.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rahul.hope.data.DataRepository

class RoomViewModelFactory constructor(private val repository: DataRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ContactsViewModel::class.java) -> ContactsViewModel(this.repository) as T
            modelClass.isAssignableFrom(ChatRoomViewModel::class.java) -> ChatRoomViewModel(this.repository) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
