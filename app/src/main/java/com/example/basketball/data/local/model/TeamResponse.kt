package com.example.basketball.data.local.model

data class TeamResponse(
    val data: TeamData
)

data class TeamData(
    val teams: List<Team>
)
data class Team(
    val tid: String,
    val tn: String,
    val ta: String,
    val logo: String,
    val color: String
)
