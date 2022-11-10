package com.atharianr.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.paging.*
import com.atharianr.storyapp.data.source.local.database.StoryDatabase
import com.atharianr.storyapp.data.source.local.entity.StoryEntity
import com.atharianr.storyapp.data.source.remote.network.ApiService
import com.atharianr.storyapp.data.source.remote.response.AddStoryResponse
import com.atharianr.storyapp.data.source.remote.response.StoriesResponse
import com.atharianr.storyapp.data.source.remote.response.StoryDetailResponse
import com.atharianr.storyapp.data.source.remote.response.vo.ApiResponse
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {
    fun getAllStoriesPaging(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getAllStories(bearerToken: String, location: Int): LiveData<ApiResponse<StoriesResponse>> =
        liveData {
            emit(ApiResponse.loading())
            try {
                val response = apiService.getAllStories(bearerToken, location)
                if (!response.error) {
                    emit(ApiResponse.success(response))
                } else {
                    emit(ApiResponse.error(response.message))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ApiResponse.error(e.message))
            }
        }

    fun getStoryDetail(
        bearerToken: String,
        id: String
    ): LiveData<ApiResponse<StoryDetailResponse>> = liveData {
        emit(ApiResponse.loading())
        try {
            val response = apiService.getStoryDetail(bearerToken, id)
            if (!response.error) {
                emit(ApiResponse.success(response))
            } else {
                emit(ApiResponse.error(response.message))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ApiResponse.error(e.message))
        }
    }

    fun addNewStory(
        bearerToken: String, image: MultipartBody.Part, desc: RequestBody
    ): LiveData<ApiResponse<AddStoryResponse>> = liveData {
        emit(ApiResponse.loading())
        try {
            val response = apiService.addNewStory(bearerToken, image, desc)
            if (!response.error) {
                emit(ApiResponse.success(response))
            } else {
                emit(ApiResponse.error(response.message))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ApiResponse.error(e.message))
        }
    }
}