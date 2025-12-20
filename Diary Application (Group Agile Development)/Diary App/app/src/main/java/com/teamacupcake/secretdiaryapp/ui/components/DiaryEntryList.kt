//package com.teamacupcake.secretdiaryapp.ui.components
//
//import androidx.compose.runtime.Composable
//import com.teamacupcake.secretdiaryapp.data.DiaryEntryViewModel
//
//@Composable
//fun DiaryEntryList(diaryEntryViewModel: DiaryEntryViewModel) {
//    // Assume entries is the list of diary entries maintained in your ViewModel
//    val entries = diaryEntryViewModel.entries.collectAsState()
//
//    entries.value.forEach { entry ->
//        DiaryEntryItem(
//            entry = entry,
//            onEntryDeleted = { deletedEntry ->
//                // Update the list to remove the deleted entry
//                diaryEntryViewModel.removeEntry(deletedEntry)
//            },
//            // ... other parameters
//        )
//    }
//}
//
//
