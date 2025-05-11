package com.example.basketball.domain.utils

import com.example.basketball.data.local.model.ScheduleItem
import com.example.basketball.data.local.model.Team

object Utility {
    fun filterUsersItemsByNamePrefix(
        scheduleItems: List<ScheduleItem>,
        prefix: String
    ): List<ScheduleItem> {
        val filtered = scheduleItems.filter {
            it.arena_name.startsWith(prefix, ignoreCase = true)
                    || it.arena_city.startsWith(prefix, ignoreCase = true)
        }
        println("Filtering with prefix: '$prefix' found ${filtered.size} items")
        return filtered
    }

    fun getFilterListByOption(
        scheduleItems: List<ScheduleItem>,
        filters: List<FilterType>
    ): List<ScheduleItem> {
        if (filters.isEmpty()) return scheduleItems

        // Map filters to the corresponding `st` values
        val allowedStValues = filters.map { filter ->
            when (filter) {
                FilterType.FUTURE -> 1
                FilterType.LIVE -> 2
                FilterType.PAST -> 3
            }
        }

        // Filter users based on matching st values
        return scheduleItems.filter { it.st in allowedStValues }
    }

    fun filterUsersByGameType(list: List<String>): List<FilterType> {
        var listType = listOf<FilterType>()
        listType = list.mapNotNull { filter ->
            try {
                val cleaned = filter.replace(" GAME", "", ignoreCase = true).trim().uppercase()
                FilterType.valueOf(cleaned) // Convert string to enum
            } catch (e: IllegalArgumentException) {
                null // Ignore invalid values
            }
        }
        return listType
    }

    fun getTeamLogo(teamId: String, teamsList: List<Team>): String {
        return teamsList.firstOrNull { it.tid == teamId }?.logo ?: ""
    }
    fun getTeam(teamId: String, teamsList: List<Team>): String {
        return teamsList.firstOrNull { it.tid == teamId }?.tn ?: ""
    }
}