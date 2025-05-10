package com.example.basketball.domain.repository

import android.content.Context
import com.example.basketball.data.local.model.ScheduleResponse
import com.example.basketball.data.local.model.Team
import com.example.basketball.data.local.model.TeamResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.Provides
import javax.inject.Inject

class ScheduleRepository @Inject constructor(){
   /* fun loadSchedule(context: Context): List<ScheduleItem> {
        val json = context.assets.open("schedule.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val wrapperType = object : TypeToken<ScheduleWrapper>() {}.type
        val scheduleWrapper: ScheduleWrapper = gson.fromJson(json, wrapperType)
        return scheduleWrapper.data.schedules
    }*/

  /*  fun loadTeams(context: Context): List<Team> {
        val json = context.assets.open("teams.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val type = object : TypeToken<TeamWrapper>() {}.type
        val wrapper: TeamWrapper = gson.fromJson(json, type)
        return wrapper.data.teams
    }*/
  fun loadSchedule(context: Context): ScheduleResponse {
        val json = context.assets.open("schedule.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val type = object : TypeToken<ScheduleResponse>() {}.type
        return gson.fromJson(json, type)
    }
    fun loadTeams(context: Context): List<Team> {
        val json = context.assets.open("teams.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val type = object : TypeToken<TeamResponse>() {}.type
        val response: TeamResponse = gson.fromJson(json, type)
        return response.data.teams
    }
}