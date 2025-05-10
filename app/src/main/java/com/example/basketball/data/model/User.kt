package com.example.basketball.data.model

import com.google.gson.Gson
import kotlin.jvm.java

data class User(
    val gender: String,
    val name: Name,
    val email: String,
    val phone: String,
    val location: Location,
    val picture: Picture
) {
    fun toJson(): String = Gson().toJson(this)  // Convert object to JSON string

    companion object {
        fun fromJson(json: String): User = Gson().fromJson(json, User::class.java)  // Convert JSON to object
    }
}