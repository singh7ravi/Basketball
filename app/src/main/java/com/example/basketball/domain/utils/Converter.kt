package com.example.basketball.domain.utils

import com.example.basketball.data.local.model.ScheduleItem
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone


object Converter {
    fun convertUtcToLocalDayLabel(utcTime: String): String {
        return try {
            val zonedUtc = ZonedDateTime.parse(utcTime)
            val localDate = zonedUtc.withZoneSameInstant(ZoneId.systemDefault())
            localDate.format(DateTimeFormatter.ofPattern("EEE MMM dd", Locale.US)).uppercase()
        } catch (e: Exception) {
            "INVALID DATE"
        }
    }

    fun convertEtTimeToDisplay(input: String): String {
        return try {
            val cleaned = input
                .replace("ET", "", ignoreCase = true) // remove timezone
                .trim()
                .replace(" ", "")                     // remove space between time and am/pm
                .uppercase()                          // uppercase entire string

            cleaned
        } catch (e: Exception) {
            "INVALID TIME"
        }
    }
    fun extractUniqueMonthYearList(scheduleList: List<ScheduleItem>): List<String> {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        return scheduleList.mapNotNull { item ->
            try {
                val date = inputFormat.parse(item.gametime)
                outputFormat.format(date!!).uppercase()
            } catch (e: Exception) {
                null
            }
        }.distinct()
    }

}