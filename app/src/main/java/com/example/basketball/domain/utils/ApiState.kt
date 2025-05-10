package com.example.basketball.domain.utils


import com.example.basketball.data.local.model.ScheduleResponse

sealed class ApiState<out T> {

    class Success<T>(val data: T) : ApiState<T>()
    class Failure(val msg: Throwable) : ApiState<Nothing>()
    object Loading : ApiState<Nothing>()
    object Empty : ApiState<Nothing>()
}