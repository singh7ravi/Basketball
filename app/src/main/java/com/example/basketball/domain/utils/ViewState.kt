package com.example.basketball.domain.utils

import com.example.basketball.data.local.model.ScheduleItem


data class ViewState(
    val filterList: List<ScheduleItem> = emptyList()
)
