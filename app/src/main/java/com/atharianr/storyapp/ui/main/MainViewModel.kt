package com.atharianr.storyapp.ui.main

import androidx.lifecycle.ViewModel
import com.atharianr.storyapp.data.source.remote.RemoteDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainViewModel(private val remoteDataSource: RemoteDataSource) : ViewModel() {
    fun getAllStories(bearerToken: String) = remoteDataSource.getAllStories(bearerToken)

    fun getDetailStory(bearerToken: String, id: String) =
        remoteDataSource.getStoryDetail(bearerToken, id)

    fun addNewStory(bearerToken: String, image: MultipartBody.Part, desc: RequestBody) =
        remoteDataSource.addNewStory(bearerToken, image, desc)
}