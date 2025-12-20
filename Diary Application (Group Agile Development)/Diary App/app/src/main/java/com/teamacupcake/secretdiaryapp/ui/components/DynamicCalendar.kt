package com.teamacupcake.secretdiaryapp.ui.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.teamacupcake.secretdiaryapp.data.DiaryEntryViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


/**
 * Composable function that displays a dynamic calendar interface, allowing users to navigate
 * through months, view entries for selected dates, and add new entries.
 *
 * @param viewModel The `DiaryEntryViewModel` to interact with for diary entries.
 * @param onNavigateToNewEntry A lambda function that navigates to a new entry screen.
 * @param navController The navigation controller used for screen navigation.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DynamicCalendar(
    viewModel: DiaryEntryViewModel,
    onNavigateToNewEntry: () -> Unit,
    navController: NavController
) {
    val entries by viewModel.entries.observeAsState()

    // Initialize variables related to the date and calendar state
    val repositoryDate = DateRepository.selectedDate
    var currentMonth by remember { mutableStateOf(YearMonth.from(repositoryDate)) }
    val daysInMonth = currentMonth.lengthOfMonth()
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    var selectedDay by remember { mutableStateOf(repositoryDate.dayOfMonth) }
    var selectedMonthYear by remember { mutableStateOf(YearMonth.from(repositoryDate)) }

    // Combines selectedDay and selectedMonthYear to form selectedDate
    var selectedDate by remember(selectedDay, selectedMonthYear) {
        mutableStateOf(LocalDate.of(selectedMonthYear.year, selectedMonthYear.month, selectedDay))
    }

    /**
     * Effect that triggers when selectedDay or selectedMonthYear changes,
     * updating the selectedDate accordingly.
     */
    LaunchedEffect(selectedDay, selectedMonthYear) {
        selectedDate = LocalDate.of(selectedMonthYear.year, selectedMonthYear.month, selectedDay)
        DateRepository.updateSelectedDate(selectedDate) // Update repository with new date
        Log.d("CalendarComposable", "Date clicked: ${DateRepository.selectedDate}")
    }

    /**
     * Retrieves and filters diary entries for the current month, then maps them to
     * a set of day values.
     */
    val daysWithEntries = remember(entries, currentMonth) {
        entries?.filter {
            LocalDate.parse(it.date).year == currentMonth.year &&
                    LocalDate.parse(it.date).month == currentMonth.month
        }?.map { LocalDate.parse(it.date).dayOfMonth }?.toSet() ?: setOf()
    }

    // Effect that triggers when the selectedDate changes, fetching entries for that date
    LaunchedEffect(selectedDate) {
        val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        viewModel.fetchEntriesForSelectedDate(formattedDate) // Fetch entries for selected date
    }

    // Trigger fetching diary entries on first composition
    LaunchedEffect(true) {
        viewModel.fetchDiaryEntries() // Call a function to fetch all diary entries
    }

    // Log entries observed
    Log.d("DynamicCalendar", "Entries observed: ${entries?.size}")

    Column(modifier = Modifier.fillMaxWidth()) {

        /**
         * Navigation for months:
         * - Buttons to navigate to previous or next month
         * - Displays the current month and year
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Previous month button
            Button(onClick = {
                currentMonth = if (currentMonth.month == Month.JANUARY) {
                    currentMonth.minusYears(1).withMonth(Month.DECEMBER.value)
                } else {
                    currentMonth.minusMonths(1)
                }
            }) { Text("<", fontWeight = FontWeight.Bold) }

            // Month and year display
            Text(text = currentMonth.format(monthYearFormatter), fontWeight = FontWeight.Bold)

            // Next month button
            Button(onClick = {
                currentMonth = if (currentMonth.month == Month.DECEMBER) {
                    currentMonth.plusYears(1).withMonth(Month.JANUARY.value)
                } else {
                    currentMonth.plusMonths(1)
                }
            }) { Text(">", fontWeight = FontWeight.Bold) }
        }

        // Weekdays header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DayOfWeek.values().forEach { day ->
                Text(
                    text = day.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    ),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .size(width = 40.dp, height = 30.dp)
                        .wrapContentSize(Alignment.Center)
                )
            }
        }

        // Days grid
        val firstDayOfWeekOfMonth = currentMonth.atDay(1).dayOfWeek.value
        var dateCounter = 1

        for (week in 0 until 5) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                (1..7).forEach { dayOfWeek ->
                    // Render day cell or empty space
                    if ((week == 0 && dayOfWeek >= firstDayOfWeekOfMonth) || (week > 0 && dateCounter <= daysInMonth)) {
                        val day = dateCounter // For clickable action
                        val isSelectedDay = day == selectedDay && currentMonth == selectedMonthYear
                        val hasEntries = day in daysWithEntries
                        Column(
                            modifier = Modifier
                                .padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 30.dp, height = 25.dp)
                                    .background(if (isSelectedDay) Color.LightGray else Color.Transparent)
                                    .clickable {
                                        selectedDay = day
                                        selectedMonthYear = currentMonth
                                        DateRepository.updateSelectedDate(LocalDate.of(currentMonth.year, currentMonth.month, selectedDay))
                                    }
                                    .wrapContentSize(Alignment.Center)
                            ) {
                                Text(text = "${dateCounter++}", fontWeight = FontWeight.Bold)
                            }

                            if (hasEntries) {
                                Box(
                                    modifier = Modifier
                                        .size(5.dp, 5.dp)
                                        .background(Color.Red)
                                        .padding(top = 3.dp)
                                )
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp))
                    }
                }
            }
        }

        // Button section at the bottom:
        // - Buttons for navigating to today's date or creating new entries
        if (!selectedDate.isEqual(LocalDate.now())) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            ) {
                Button(
                    onClick = {
                        val today = LocalDate.now()
                        selectedDay = today.dayOfMonth
                        selectedMonthYear = YearMonth.from(today)
                        currentMonth = YearMonth.from(today)
                        DateRepository.updateSelectedDate(today) // Update repository with today's date
                    }
                ) {
                    Text("Return")
                }
                Spacer(modifier = Modifier.width(8.dp).padding(vertical = 8.dp))

                Button(onClick = {
                    onNavigateToNewEntry()
                }) {
                    val dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
                    Text("Add New Entry For ${selectedDate.format(dateFormatter)}")
                }
            }
        } else {
            // Row of buttons for adding a new entry or showing the current streak
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            ) {
                Button(onClick = {
                    onNavigateToNewEntry()
                }) {
                    Text("Add New Entry For Today")
                }

                Spacer(Modifier.width(10.dp))

                Button(onClick = {
                    if (DateRepository.todaysStreak) {
                      //  DateRepository.todaysStreak = false // Simulating streak reset
                    }
                }) {
                    Text("Streak: ${DateRepository.currentStreak}")
                }
            }
        }

        // Display entries for the selected date
        val entriesForSelectedDate by viewModel.selectedDateEntries.observeAsState(initial = emptyList())

        if (entriesForSelectedDate.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No entries found",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            val sortedEntries = entriesForSelectedDate.sortedBy { it.timestamp }
            LazyColumn {
                items(sortedEntries) { entry ->
                    DiaryEntryItem(
                        entry = entry,
                        onPasswordEntered = { /* handle password */ },
                        onEntryClick = { /* handle entry click */ },
                        onEntryDeleted = { },
                        onNavigateToHome = { },
                        navController = navController
                    )
                    Log.d("Image:", "${entry.imageUrl}")
                }
            }
        }
    }
}
