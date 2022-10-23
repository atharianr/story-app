package com.atharianr.storyapp.data.source.remote.response

data class StoriesResponse(
    val error: Boolean = false,
    val message: String = "",
    val listStory: List<Story> = listOf()
)