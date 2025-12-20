package com.teamacupcake.secretdiaryapp.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.teamacupcake.secretdiaryapp.data.DiaryEntry
import com.teamacupcake.secretdiaryapp.data.DiaryEntryViewModel
import com.teamacupcake.secretdiaryapp.ui.components.DiaryEntryItem

@Composable
fun DiaryEntriesScreen(
    diaryEntryViewModel: DiaryEntryViewModel = viewModel(),
    onEntryClick: (DiaryEntry) -> Unit,
    onPasswordEntered: (DiaryEntry, String) -> Unit,
    navController: NavController
) {
    // Observe the LiveData from the ViewModel and convert it to state for Compose
    val entries by diaryEntryViewModel.entries.observeAsState(initial = emptyList())
    // Observe the counter to force recomposition
    val force = diaryEntryViewModel.recompositionCounter.value

    // Use LaunchedEffect with no keys to run once when the composable enters the composition
    LaunchedEffect(Unit) {
        // Fetch diary entries when the composable enters the composition
        diaryEntryViewModel.fetchDiaryEntries()
    }

    LazyColumn {
            // Use the Elvis operator to ensure entries is treated as non-nullable
            items(entries ?: emptyList()) { entry ->
                DiaryEntryItem(
                    entry = entry,
                    onPasswordEntered = { password -> onPasswordEntered(entry, password) },
                    onEntryClick = { onEntryClick(entry) },
                    onEntryDeleted = { deletedEntry ->
                        entry.title = "DELETED"
                        entry.content = "DELETED"
                        entry.mood = ""



//                        // This will trigger the deletion logic in your ViewModel
                        diaryEntryViewModel.deleteDiaryEntry(entry.docId)
                        diaryEntryViewModel.fetchDiaryEntries()
                    },
                    onNavigateToHome = {},
                    navController = navController

                )
        }
    }
}



