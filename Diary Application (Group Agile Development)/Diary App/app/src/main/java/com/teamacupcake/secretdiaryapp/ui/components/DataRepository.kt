package com.teamacupcake.secretdiaryapp.ui.components

import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
/**
 * A repository to manage date-related information, including a selected date,
 * current streak, today's streak, and the last post date.
 *
 * This repository provides methods to load and save preferences related to date tracking,
 * reset today's streak when necessary, and update the selected date.
 */
object DateRepository {

    /** The date selected by the user, defaulted to today's date. */
    var selectedDate: LocalDate = LocalDate.now()
        private set // Prevents modification from outside the class

    /** The current streak of consecutive posts, using a mutable state holder. */
    var currentStreak: Int by mutableStateOf(0)

    /** Boolean indicating if a post has been made today, using a mutable state holder. */
    var todaysStreak: Boolean by mutableStateOf(false)

    /** The date of the last post made, initialized to yesterday's date. */
    var lastPostDate: LocalDate by mutableStateOf(LocalDate.now().minusDays(1))

    /**
     * Loads date-related preferences from the shared preferences.
     *
     * @param context The context from which to retrieve preferences.
     */
    fun loadPreferences(context: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)

        // Load the last post date from shared preferences
        lastPostDate = LocalDate.parse(preferences.getString("lastPostDate", LocalDate.now().toString()))

        // Load the current streak from shared preferences
        currentStreak = preferences.getInt("currentStreak", 0)

        // Load whether a post has been made today from shared preferences
        todaysStreak = preferences.getBoolean("todaysStreak", false)

        // Reset today's streak if needed when the app starts
        resetTodaysStreakIfNeeded()
    }

    /**
     * Saves date-related preferences to the shared preferences.
     *
     * @param context The context to use for accessing shared preferences.
     */
    fun savePreferences(context: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        with(preferences.edit()) {
            putString("lastPostDate", lastPostDate.toString()) // Save the last post date
            putInt("currentStreak", currentStreak) // Save the current streak
            putBoolean("todaysStreak", todaysStreak) // Save today's streak status
            apply() // Apply the changes
        }
    }

    /**
     * Updates the selected date and resets today's streak if needed.
     *
     * @param newDate The new date to set as selected.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateSelectedDate(newDate: LocalDate) {
        selectedDate = newDate // Update the selected date
        Log.d("DateRepository", "Selected date updated: $selectedDate") // Log the new date
        resetTodaysStreakIfNeeded() // Check and reset today's streak if necessary
    }

    /**
     * Resets today's streak and current streak if needed.
     *
     * Resets the streak if more than a day has passed since the last post.
     * Sets today's streak to false if today is after the last post date.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun resetTodaysStreakIfNeeded() {
        val today = LocalDate.now() // Get today's date
        val daysBetween = ChronoUnit.DAYS.between(lastPostDate, today) // Days between last post and today

        if (daysBetween >= 2) {
            currentStreak = 0 // Reset current streak to 0 if two days or more have passed
            Log.d("DateRepository", "Current streak reset to 0 as last post was two days ago or more.")
        }

        if (today.isAfter(lastPostDate)) {
            todaysStreak = false // Set today's streak to false if today is after last post date
        }
    }
}
