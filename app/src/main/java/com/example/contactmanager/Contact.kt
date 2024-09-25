package com.example.contactmanager

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val nickname: String,
    val phoneNumber: String,
    val email: String,
    val relationship: String
)
