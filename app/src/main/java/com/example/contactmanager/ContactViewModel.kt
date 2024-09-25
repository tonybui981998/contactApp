package com.example.contactmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val contactDao: ContactDao = ContactDatabase.getDatabase(application).contactDao()

    val allContacts: LiveData<List<Contact>> = contactDao.getAllContacts()

    fun insert(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            contactDao.insert(contact)
        }
    }

    fun update(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            contactDao.update(contact)
        }
    }

    fun delete(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            contactDao.delete(contact)
        }
    }

    suspend fun getContactById(contactId: Int): Contact? {
        return withContext(Dispatchers.IO) {
            contactDao.getContactById(contactId)
        }
    }
}
