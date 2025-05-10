package com.example.basketball.data.local.model

data class ScheduleResponse(
    val data: ScheduleData
)

data class ScheduleData(
    val schedules: List<ScheduleItem>
)

data class ScheduleItem(
    val arena_name: String,
    val arena_city: String,
    val st: Int,
    val stt: String,
    val gametime: String,
    val bd: BroadcasterData?, // Nullable in case missing
    val h: TeamInfo,
    val v: TeamInfo
)

data class BroadcasterData(
    val b: List<Broadcaster>
)

data class Broadcaster(
    val scope: String
)

data class TeamInfo(
    val tid: String,
    val ta: String,
    val s: String,
    val tn: String,
    val tc: String,
    val logo: String,
)
