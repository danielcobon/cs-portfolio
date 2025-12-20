package com.teamacupcake.secretdiaryapp.ui.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
fun getTimeAgo(timestamp: Long): String {
    val now = LocalDateTime.now()
    val entryTime = Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

    val secondsAgo = ChronoUnit.SECONDS.between(entryTime, now)
    val minutesAgo = secondsAgo / 60
    val hoursAgo = minutesAgo / 60
    val daysAgo = hoursAgo / 24

    return when {
        daysAgo > 0 -> "$daysAgo day${if (daysAgo > 1) "s" else ""} ago"
        hoursAgo > 0 -> "$hoursAgo hour${if (hoursAgo > 1) "s" else ""} ago"
        minutesAgo > 0 -> "$minutesAgo minute${if (minutesAgo > 1) "s" else ""} ago"
        else -> "Just now"
    }
}
