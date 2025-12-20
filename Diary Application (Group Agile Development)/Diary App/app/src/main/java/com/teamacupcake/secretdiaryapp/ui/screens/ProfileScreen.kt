package com.teamacupcake.secretdiaryapp.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.teamacupcake.secretdiaryapp.data.ProfileViewModel
import com.teamacupcake.secretdiaryapp.data.UserProfile
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import com.teamacupcake.secretdiaryapp.ui.utils.isOnline

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val context = LocalContext.current
    val userProfile by viewModel.userProfile.observeAsState(UserProfile())
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    // Use LaunchedEffect to fetch user profile once when the Composable enters the composition
    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.fetchUserProfile(userId)
        }
    }
    // State to hold the URI of the selected image
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // State to manage editing mode for the username
    var isEditingUsername by remember { mutableStateOf(false) }
    var editedUsername by remember { mutableStateOf(userProfile.username) }

    // Launcher to pick an image
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri // Update the state with the selected image URI
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Edit Profile", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (isEditingUsername) {
            // TextField for editing the username
            TextField(
                value = editedUsername,
                onValueChange = { editedUsername = it },
                label = { Text("Username") },
                singleLine = true
            )
            Button(
                onClick = {
                    if (userId != null) {
                        viewModel.updateUsername(userId, editedUsername)
                        if (!isOnline(context)) {
                            Toast.makeText(context, "Username updated locally. It will be synced when online", Toast.LENGTH_LONG).show()

                        }
                        isEditingUsername = false // Exit editing mode
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Save Username")
            }
        } else {
            // Display the username with an option to edit
            Row(
                modifier = Modifier.clickable { isEditingUsername = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Username: " +
                    userProfile.username,
                    style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Filled.Edit, "Edit", Modifier.size(20.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display the selected image or a placeholder
        val imagePainter = when {
            selectedImageUri != null -> rememberImagePainter(selectedImageUri)
            userProfile.profilePictureUrl.isNotEmpty() -> rememberImagePainter(userProfile.profilePictureUrl)
            else -> null
        }

        if (imagePainter != null) {
            Image(
                painter = imagePainter,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(200.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile Picture Placeholder",
                modifier = Modifier.size(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { pickImageLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change Profile Picture")
        }

        // Show the save button only if a new image has been selected
        if (selectedImageUri != null) {
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    selectedImageUri?.let { uri ->
                        val inputStream = context.contentResolver.openInputStream(uri)
                        inputStream?.let { stream ->
                            if (userId != null) {
                                viewModel.changeProfilePicture(userId, stream, uri.lastPathSegment ?: "profilePic.jpg")
                                if (!isOnline(context)) {
                                    Toast.makeText(context, "Profile Picture updated locally. It will be synced when online", Toast.LENGTH_LONG).show()

                                }
                                selectedImageUri = null // Reset the URI after saving
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Profile Picture")
            }
        }
    }
}

