package com.teamacupcake.secretdiaryapp.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.teamacupcake.secretdiaryapp.data.DiaryEntry
import com.teamacupcake.secretdiaryapp.data.DiaryEntryViewModel


@Composable
    fun DiaryEntryItem(
        entry: DiaryEntry,
        navController: NavController,
        onEntryDeleted: (DiaryEntry) -> Unit, // Add this callback to handle deletion
        onPasswordEntered: (String) -> Unit,
        onEntryClick: (DiaryEntry) -> Unit,
        searchQuery: String = "",
        onNavigateToHome: () -> Unit,
        isFriendEntry: Boolean = false // New parameter to identify friend's entries

    ) {
        //var _diaryEntryViewModel.entries by remember { mutableStateOf(DiaryEntry) }

        var showDeleteConfirmDialog by remember { mutableStateOf(false) } // New state for delete confirmation
        var isDialogVisible by remember { mutableStateOf(false) }
        var enteredPassword by remember { mutableStateOf("") }
        var isPasswordCorrect by remember { mutableStateOf(false) }
        var remainingAttempts by remember { mutableStateOf(5) }
        var lastAttemptTime by remember { mutableStateOf(0L) } // Timestamp in milliseconds
        var showMenu by remember { mutableStateOf(false) }
        val diaryEntryViewModel: DiaryEntryViewModel = viewModel()
      //  var entryDeleted by remember { mutableStateOf(false) }
        //val entries by diaryEntryViewModel.entries.observeAsState(initial = emptyList())

        val entries by diaryEntryViewModel.entries.observeAsState(initial = emptyList())
        // Place this state at the top level of your composable where the diary entry is shown
        val entryDeleted by diaryEntryViewModel.entryDeleted.observeAsState()


//        LaunchedEffect(key1 = true) {
//            diaryEntryViewModel.fetchDiaryEntries()
//            diaryEntryViewModel.fetchDiaryEntriesWithImages()
//            diaryEntryViewModel.fetchEntryImagesAndUpdate()
//        }
        LaunchedEffect(entryDeleted) {
            if (entryDeleted == true) {
                diaryEntryViewModel.fetchDiaryEntries() // Fetch entries again after one is deleted
                //entryDeleted = false // Reset the state
            }
        }

        // This block will be executed every time the `entries` LiveData changes.
            // For example, when an entry is added, updated, or deleted.

            // Check if an entry has been deleted:
            // Assuming you have some logic or state to determine if an entry has been deleted

//                if (entryDeleted) {
//                    // Perform an action if an entry is deleted
//                    // For example, show a Snackbar, update UI, etc.
//                    diaryEntryViewModel.fetchDiaryEntries()
//                    entryDeleted = false
//                }
//        }


        val secretText =
            if (entry.passLocked && remainingAttempts == 0) "LOCKED" else if (entry.passLocked) "Secret Entry" else "Not Secret"

        val backgroundColor = if (entry.passLocked && remainingAttempts == 0) {
            Color.Red // Display locked entries with a red background color
        } else {
            Color(0xFFFFB6C1)
        }

        val textColor = if (entry.passLocked && remainingAttempts == 0) {
            Color.Gray // Display locked entries with a different text color
        } else {
            Color.Black
        }

        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = true
                )
                .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            if (!isFriendEntry) { // Only show the options menu for the user's own entries

                IconButton(modifier = Modifier
                    .align(Alignment.TopEnd),
                        onClick = { showDeleteConfirmDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
                if (showDeleteConfirmDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteConfirmDialog = false },
                        title = { Text("Delete Entry") },
                        text = { Text("Are you sure you want to delete this diary entry?") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    diaryEntryViewModel.deleteDiaryEntry(entry.docId)
                                    //diaryEntryViewModel.removeEntry(entry)
                                   // diaryEntryViewModel.fetchDiaryEntries()

                                    showDeleteConfirmDialog = false
                                    // Optionally notify the caller that the entry has been deleted
                                    // onEntryDeleted(entry)
                                }
                            ) {
                                Text("Delete")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showDeleteConfirmDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                // Delete confirmation dialog
                if (showDeleteConfirmDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteConfirmDialog = false
                            Log.d("DiaryEntryItem", "Delete dialog dismissed")
                        },
                        title = { Text("Confirm Delete") },
                        text = { Text("Are you sure you want to delete this entry?") },
                        confirmButton = {
                            Button(onClick = {
                                Log.d("DiaryEntryItem", "Confirm delete clicked for entry: ${entry.docId}")

                                diaryEntryViewModel.deleteDiaryEntry(entry.docId)
                                diaryEntryViewModel.removeEntry(entry)
                               // onEntryDeleted(entry) // Notify that the entry should be removed
                                navController.navigate("Refresh") // Add the route name of your RefreshScreen here

                                onNavigateToHome()
                                showDeleteConfirmDialog = false // Close the dialog

                            }) {
                                Text("Confirm")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showDeleteConfirmDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }

            Column(modifier = Modifier.padding(8.dp)) {
                // Print entry title if it's not locked, if the password is correct, or if there are remaining attempts
                if (!entry.passLocked || isPasswordCorrect || remainingAttempts > 0) {
                    Text(
                        text = "Title: ${entry.title}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        color = textColor
                    )
                    //                HighlightSearchResult(
                    //                    text = entry.title,
                    //                    query = searchQuery,
                    //                    textStyle = MaterialTheme.typography.titleLarge
                    //                )
                }

                // Print whether the entry is secret or locked
                //            Text(
                //                text = secretText,
                //                style = MaterialTheme.typography.bodyMedium,
                //                color = textColor
                //            )

                // Check if the entry is locked and the password is incorrect
                if (entry.passLocked && remainingAttempts > 0 && !isPasswordCorrect) {
                    Button(
                        onClick = { isDialogVisible = true },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text("Enter Password")
                    }
                }

                // Display "LOCKED" only when attempts reach zero
                if (entry.passLocked && remainingAttempts == 0) {


                }


                // Display entry content if the password is correct or the entry is not locked
                if ((!entry.passLocked || isPasswordCorrect) && remainingAttempts > 0) {
                    // Entry content
                    Text(
                        text = "Your Diary Entry:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .padding(start = 8.dp),
                        color = textColor
                    )
                    //                HighlightSearchResult(
                    //                    text = entry.content,
                    //                    query = searchQuery,
                    //                    textStyle = MaterialTheme.typography.bodyMedium
                    //                )


                    Text(
                        text = entry.content,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(start = 8.dp, bottom = 8.dp)
                            .fillMaxWidth(),
                        color = textColor
                    )
                    //                Column(
                    //                    modifier = Modifier
                    //                        .padding(8.dp)
                    //                        .fillMaxSize()) {

                    //}
                    diaryEntryViewModel.fetchDiaryEntries()
                    diaryEntryViewModel.fetchDiaryEntriesWithImages()
                    diaryEntryViewModel.fetchEntryImagesAndUpdate()
                    // Entry image if available
                    entry.imageUrl?.let { imageUrl ->
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Log.d("Image:", "${entry.imageUrl}")
                            Image(
                                painter = rememberAsyncImagePainter(imageUrl),
                                contentDescription = "Picked image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .heightIn(max = 200.dp)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Display mood only when the entry is not locked or the correct password is entered and the entry content is shown
                        if ((!entry.passLocked || isPasswordCorrect) && entry.mood.isNotEmpty()) {
                            Text(
                                text = "Mood: ${entry.mood}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = textColor
                            )
                        }

                        Text(
                            text = entry.date,
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor,
                            modifier = Modifier
                                //.align(Horizontal(End)

                        )

                    }
                }
            }
        }


        if (isDialogVisible) {
            AlertDialog(
                onDismissRequest = { isDialogVisible = false },
                title = { Text(text = "Enter Password") },
                text = {
                    Column {
                        TextField(
                            value = enteredPassword,
                            onValueChange = { enteredPassword = it },
                            label = { Text("Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        if (remainingAttempts < 5) {
                            Text(
                                text = "Attempts left: $remainingAttempts",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Red
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (enteredPassword == entry.password) {
                                isPasswordCorrect = true
                                onPasswordEntered(enteredPassword)
                                isDialogVisible = false
                            } else {
                                remainingAttempts--
                                if (remainingAttempts == 0) {
                                    // Lockout the user
                                    lastAttemptTime = System.currentTimeMillis()
                                    // Optionally, you could display a message indicating the lockout
                                    isDialogVisible = false
                                } else {
                                    // Show error message or handle incorrect password
                                }
                            }
                        }
                    ) {
                        Text("Submit")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { isDialogVisible = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }


