package com.atharianr.storyapp.data.source.remote.response

data class BaseResponse(
    val error: Boolean,
    val message: String,
    val result: ResultResponse
)