package com.atharianr.storyapp.data.source.remote.response

import com.atharianr.storyapp.data.source.local.entity.StoryEntity

data class StoriesResponse(
    val error: Boolean = false,
    val message: String = "",
    val listStory: List<StoryEntity> = listOf()
)