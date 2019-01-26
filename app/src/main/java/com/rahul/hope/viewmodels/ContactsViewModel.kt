package com.rahul.hope.viewmodels

import androidx.lifecycle.ViewModel
import com.rahul.hope.data.DataRepository

class ContactsViewModel(repository: DataRepository) : ViewModel() {
    var standardEmergencyList = repository.getStandardContacts()
    var personalEmergencyList = repository.getPersonalContacts()
}