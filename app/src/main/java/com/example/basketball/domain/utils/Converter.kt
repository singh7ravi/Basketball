package com.example.basketball.domain.utils

import com.example.basketball.data.local.model.ScheduleItem
import kotlin.collections.any
import kotlin.collections.filter
import kotlin.text.lowercase
import kotlin.text.startsWith


object Converter {

    fun filterUsersItemsByNamePrefix(
        scheduleItems: List<ScheduleItem>,
        prefix: String
    ): List<ScheduleItem> {
        return scheduleItems.filter {
            it.arena_name.startsWith(prefix, ignoreCase = true)
                    || it.arena_city.startsWith(prefix, ignoreCase = true)
        }
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

}