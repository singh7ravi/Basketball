package com.example.basketball.data.model


data class UserResponse(val results: List<User>)

data class Name(val first: String, val last: String, val title: String)
data class Location(val street: Street, val city: String, val country: String)
data class Street(val number: Int, val name: String)
data class Picture(val medium: String, val large: String)