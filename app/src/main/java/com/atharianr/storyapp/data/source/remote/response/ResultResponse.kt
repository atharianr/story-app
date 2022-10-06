package com.atharianr.storyapp.data.source.remote.response

data class ResultResponse(
    val userId: String,
    val name: String,
    val token: String,

    val id: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Double,
    val lon: Double
)