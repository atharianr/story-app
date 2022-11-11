package com.atharianr.storyapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.atharianr.storyapp.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getAllStories(bearerToken: String, location: Int = 0) =
        storyRepository.getAllStories(bearerToken, location)

    fun getDetailStory(bearerToken: String, id: String) =
        storyRepository.getStoryDetail(bearerToken, id)

    fun addNewStory(bearerToken: String, image: MultipartBody.Part, desc: RequestBody) =
        storyRepository.addNewStory(bearerToken, image, desc)

    fun getAllStoriesPaging() = storyRepository.getAllStoriesPaging()
}