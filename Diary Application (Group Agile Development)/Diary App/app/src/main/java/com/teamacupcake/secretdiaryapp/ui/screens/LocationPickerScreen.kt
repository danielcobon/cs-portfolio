package com.teamacupcake.secretdiaryapp.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.teamacupcake.secretdiaryapp.data.DiaryEntryViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LocationPickerScreen(
    navController: NavController,
    diaryEntryViewModel: DiaryEntryViewModel
) {
    val onLocationSelected: (Double, Double, String) -> Unit = { lat, lon, name ->
        // Correctly prepare the data string
        val locationData = "$lat,$lon,$name"
        // Use a consistent key for setting and observing the data. Assuming "locationDataKey" is the key.
        navController.previousBackStackEntry?.savedStateHandle?.set("locationKey", locationData)

        // Log the selection
        Log.v("LocationPickerScreen", "Location selected: Lat=$lat, Lon=$lon, Name=$name")

        // Optionally navigate back after setting the data
       // navController.popBackStack()
    }

    FullScreenMapComposable(
        modifier = Modifier.fillMaxSize(), // Fill the entire screen with the map
        navController = navController,
        onLocationSelected = onLocationSelected,
        diaryEntryViewModel = diaryEntryViewModel
    )
}

