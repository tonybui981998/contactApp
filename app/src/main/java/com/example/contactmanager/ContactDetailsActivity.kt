package com.example.contactmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch

class ContactDetailsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val contactViewModel = ViewModelProvider(this, ContactViewModelFactory(application))
                .get(ContactViewModel::class.java)

            val contactId = intent.getIntExtra("CONTACT_ID", -1)
            if (contactId != -1) {
                var contact by remember { mutableStateOf<Contact?>(null) }

                LaunchedEffect(contactId) {
                    contact = contactViewModel.getContactById(contactId)
                }

                contact?.let {
                    ContactDetailsScreen(it, contactViewModel)
                } ?: run {
                    Text("Loading...", Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun ContactDetailsScreen(contact: Contact, contactViewModel: ContactViewModel) {
    var name by remember { mutableStateOf(contact.name) }
    var nickname by remember { mutableStateOf(contact.nickname) }
    var phoneNumber by remember { mutableStateOf(contact.phoneNumber) }
    var email by remember { mutableStateOf(contact.email) }
    var relationship by remember { mutableStateOf(contact.relationship) }

    val context = LocalContext.current as? ComponentActivity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Back button
        Button(
            onClick = { context?.finish() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
            modifier = Modifier.align(Alignment.Start).padding(bottom = 16.dp)
        ) {
            Text("Back", color = Color.White)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Gray, shape = CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text("Nickname") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = relationship,
            onValueChange = { relationship = it },
            label = { Text("Relationship") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Save button
            Button(onClick = {
                val updatedContact = contact.copy(
                    name = name,
                    nickname = nickname,
                    phoneNumber = phoneNumber,
                    email = email,
                    relationship = relationship
                )
                contactViewModel.update(updatedContact)

                // Finish activity and go back to the previous screen (Contact List)
                context?.finish()
            }) {
                Text("Save")
            }

            // Delete button
            Button(onClick = {
                contactViewModel.delete(contact)
                context?.finish()
            }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)) {
                Text("Delete", color = Color.White)
            }
        }
    }
}
