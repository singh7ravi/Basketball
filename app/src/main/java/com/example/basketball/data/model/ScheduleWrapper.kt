package com.example.basketball.data.model

import com.example.basketball.data.local.model.ScheduleItem

data class ScheduleWrapper(
    val data: ScheduleData
)

data class ScheduleData(
    val schedules: List<ScheduleItem>
)