package com.example.basketball.domain.repository

import android.content.Context
import android.util.Log
import com.example.basketball.data.local.model.ScheduleItem
import com.example.basketball.data.local.model.ScheduleResponse
import com.example.basketball.data.local.model.Team
import com.example.basketball.data.local.model.TeamResponse
import com.example.basketball.domain.utils.ApiState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.Provides
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Response
import javax.inject.Inject

class ScheduleRepository @Inject constructor(){

    fun loadSchedule(context: Context): Flow<ApiState<List<ScheduleItem>>> = flow {
        emit(ApiState.Loading)
        try {
            val json = context.assets.open("schedule.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val type = object : TypeToken<ScheduleResponse>() {}.type
            val scheduleResponse: ScheduleResponse = gson.fromJson(json, type)
            emit(ApiState.Success(scheduleResponse.data.schedules))
        } catch (e: Exception) {
            emit(ApiState.Failure(e))
        }
    }

    fun loadTeams(context: Context): List<Team> {
        val json = context.assets.open("teams.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val type = object : TypeToken<TeamResponse>() {}.type
        val response: TeamResponse = gson.fromJson(json, type)
        return response.data.teams
    }
}