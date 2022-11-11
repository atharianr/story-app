package com.atharianr.storyapp.utils

import com.atharianr.storyapp.data.source.local.entity.StoryEntity
import com.atharianr.storyapp.data.source.remote.response.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {

    const val dummyToken = "Bearer token"
    const val dummyId = "example_id"
    val dummyDescription = "desc".toRequestBody("text/plain".toMediaType())
    val dummyImageMultipart = MultipartBody.Part.create("example_images".toRequestBody())

    fun generateAuthData(): AuthResponse =
        AuthResponse(
            false,
            "Success",
            AuthResponse.LoginResult("id", "Rian", "Bearer Token")
        )

    fun generateStoriesData(): StoriesResponse {
        val storyList = ArrayList<StoryEntity>()
        for (i in 0..10) {
            val story = StoryEntity(
                "ID $i",
                "Name $i",
                "Desc $i",
                "Url $i",
            )
            storyList.add(story)
        }
        return StoriesResponse(false, "Success", storyList)
    }

    fun generateStoryDetailData(): StoryDetailResponse {
        return StoryDetailResponse(
            false, "Success", Story(
                "",
                "",
                "",
                "",
                "",
                0.0,
                0.0,
            )
        )
    }

    fun generateAddStoryData(): AddStoryResponse = AddStoryResponse(false, "Success")
}