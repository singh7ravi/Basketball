package com.example.basketball.domain.utils

import com.example.basketball.data.local.model.Team
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object Utility {
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

    fun getTeamLogo(teamId: String, teamsList: List<Team>): String {
        return teamsList.firstOrNull { it.tid == teamId }?.logo ?: ""
    }
    fun getTeam(teamId: String, teamsList: List<Team>): String {
        return teamsList.firstOrNull { it.tid == teamId }?.tn ?: ""
    }

}