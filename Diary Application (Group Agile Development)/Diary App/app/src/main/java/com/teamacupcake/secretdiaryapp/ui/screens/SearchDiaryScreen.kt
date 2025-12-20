package com.teamacupcake.secretdiaryapp.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.storage.FirebaseStorage
import com.teamacupcake.secretdiaryapp.data.DiaryEntry
import com.teamacupcake.secretdiaryapp.data.DiaryEntryViewModel
import com.teamacupcake.secretdiaryapp.data.UserProfile
import com.teamacupcake.secretdiaryapp.ui.components.DiaryEntryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


//    @Composable
//    fun SearchResults(diaryEntryViewModel: DiaryEntryViewModel, navController: NavController) {
//        val searchResults by diaryEntryViewModel.searchResults.observeAsState(initial = emptyList())
//
//        LazyColumn {
//            items(searchResults) { entry ->
//                DiaryEntryItem(
//                    entry = entry,
//                    onPasswordEntered = { /* implement password handling logic */ },
//                    onEntryClick = { /* implement entry click handling logic */ },
//                    onEntryDeleted = { deletedEntry ->
//                        // Update the list to remove the deleted entry
//                        diaryEntryViewModel.removeEntry(deletedEntry)},
//                    onNavigateToHome = {},
//                    navController = navController
//                )
//
//            }
//        }
//    }

    @Composable
    fun SearchDiaryScreenComposable(diaryEntryViewModel: DiaryEntryViewModel = viewModel(), navController: NavController) {
        var searchQuery by remember { mutableStateOf("") }

        Column {
            TextField(
                value = searchQuery,
                onValueChange = { query ->
                    // Filter out newline characters from the input
                    searchQuery = query.filter { it != '\n' }
                    diaryEntryViewModel.searchDiaryEntries(searchQuery)
                },
                label = { Text("Search") },
                singleLine = true, // This ensures the text field does not expand to accommodate new lines
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done) // Changes the Enter key to Done, further reinforcing the single-line behavior
            )
            @Composable
            fun SearchResults(diaryEntryViewModel: DiaryEntryViewModel, navController: NavController
            ) {
                val searchResults by diaryEntryViewModel.searchResults.observeAsState(initial = emptyList())




                LazyColumn {
                    items(searchResults) { entry ->
                        // Now check if storageReference is valid before fetching the image from the storage reference
                        entry.storageReference?.takeIf { it.isNotBlank() }?.let { storageReference ->
                            val storageRef = FirebaseStorage.getInstance().reference.child(storageReference)
                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                entry.imageUrl = uri.toString()
                            }.addOnFailureListener {
                                Log.e("DiaryEntryDialog", "Failed to fetch entry image.", it)
                            }
                        } ?: run {
                            // Handle the case where storageReference is null or empty
                            Log.e("DiaryEntryDialog", "Storage reference is null or empty.")
                        }
                        DiaryEntryItem(
                            entry = entry,
                            onPasswordEntered = { /* implement password handling logic */ },
                            onEntryClick = { /* implement entry click handling logic */ },
                            onEntryDeleted = { deletedEntry ->
                                // Update the list to remove the deleted entry
                                diaryEntryViewModel.removeEntry(deletedEntry)},
                            onNavigateToHome = {},
                            navController = navController
                        )

                    }
                }
            }
            // Display search results
            SearchResults(diaryEntryViewModel, navController)
        }
    }

// Helper composable to fetch image URL
@Composable
fun fetchImageUrl(storageReference: String?): String {
    var imageUrl by remember { mutableStateOf("") }

    LaunchedEffect(storageReference) {
        // Check if storageReference is valid before fetching the image
        if (!storageReference.isNullOrBlank()) {
            try {
                val uri = withContext(Dispatchers.IO) {
                    FirebaseStorage.getInstance().reference.child(storageReference).downloadUrl.await()
                }
                imageUrl = uri.toString()
            } catch (e: Exception) {
                Log.e("SearchResults", "Failed to fetch image for entry.", e)
            }
        }
    }

    return imageUrl
}


