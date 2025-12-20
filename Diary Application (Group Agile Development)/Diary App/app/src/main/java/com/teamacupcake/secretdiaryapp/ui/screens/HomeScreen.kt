package com.teamacupcake.secretdiaryapp.ui.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.teamacupcake.secretdiaryapp.data.DiaryEntry
import com.teamacupcake.secretdiaryapp.data.DiaryEntryViewModel
import com.teamacupcake.secretdiaryapp.ui.components.DateRepository
import com.teamacupcake.secretdiaryapp.ui.components.DiaryEntryItem
import com.teamacupcake.secretdiaryapp.ui.components.DynamicCalendar


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun HomePage(
        context: Context,
        onNavigateToNewEntry: () -> Unit,
        viewModel: DiaryEntryViewModel, // Assuming DynamicCalendar uses it
        navController: NavController

    ) {
        //val entries: MutableLiveData<List<DiaryEntry>?>

        val entries by viewModel.entries.observeAsState(initial = emptyList())
        // Trigger a fetch operation every time the HomePage appears
        LaunchedEffect(entries) {
            viewModel.fetchDiaryEntries()
            viewModel.fetchDiaryEntriesWithImages()
            viewModel.fetchEntryImagesAndUpdate()
        }
        Column(modifier = Modifier) {

            DynamicCalendar(viewModel,onNavigateToNewEntry, navController)


        }

        SideEffect {
            DateRepository.savePreferences(context) // Save preferences when Home is accessed
        }

    }

