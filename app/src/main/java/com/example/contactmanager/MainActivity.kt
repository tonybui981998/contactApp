package com.example.contactmanager

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

class MainActivity : ComponentActivity() {

    private val contactViewModel: ContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactListScreen(contactViewModel)
        }
    }
}

@Composable
fun ContactListScreen(contactViewModel: ContactViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }

    val context = LocalContext.current
    val contacts by contactViewModel.allContacts.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        // Header with title and contact count
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "Phone",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "${contacts.size} contacts with phone numbers",
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // Action buttons (Add and Search)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = {
                context.startActivity(Intent(context, AddContactActivity::class.java))
            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Contact")
            }

            IconButton(onClick = { /* Implement search functionality here */ }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Contacts")
            }
        }

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Contacts") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Contact list with scroll
        Text(text = "Contact List:", modifier = Modifier.padding(bottom = 8.dp))

        val filteredContacts = contacts.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.phoneNumber.contains(searchQuery)
        }

        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(filteredContacts) { contact ->
                ContactRow(contact = contact, onDetailsClick = {
                    val intent = Intent(context, ContactDetailsActivity::class.java)
                    intent.putExtra("CONTACT_ID", contact.id)
                    context.startActivity(intent)
                })
            }
        }
    }
}

@Composable
fun ContactRow(contact: Contact, onDetailsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White)
            .padding(16.dp)
            .clickable { onDetailsClick() }
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.Gray, shape = CircleShape)
                .padding(8.dp)
        ) {
            Text(
                text = contact.name.first().toString(),
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = contact.name, fontSize = 18.sp)
            Text(text = contact.phoneNumber, fontSize = 14.sp, color = Color.Gray)
        }
        IconButton(
            onClick = onDetailsClick,
            modifier = Modifier.size(35.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "View Details",
                modifier = Modifier.size(35.dp)
            )
        }
    }
}
