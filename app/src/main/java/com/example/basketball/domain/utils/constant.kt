package com.example.basketball.domain.utils

import com.example.basketball.data.model.FilterCategory


const val FILTER_FUTURE = "FUTURE GAME"
const val FILTER_LIVE = "LIVE GAME"
const val FILTER_PAST = "PAST GAME"
val filterCategories = listOf(
    FilterCategory(FILTER_FUTURE, false),
    FilterCategory(FILTER_LIVE, false),
    FilterCategory(FILTER_PAST, false),
)
enum class FilterType {
    FUTURE,
    LIVE,
    PAST
}
const val BASE_URL = "https://randomuser.me/"

