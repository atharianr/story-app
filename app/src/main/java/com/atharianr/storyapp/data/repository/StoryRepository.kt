package com.atharianr.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.atharianr.storyapp.data.source.remote.network.ApiService
import com.atharianr.storyapp.data.source.local.database.StoryDatabase
import com.atharianr.storyapp.data.source.local.entity.StoryEntity
import com.atharianr.storyapp.data.source.remote.response.AddStoryResponse
import com.atharianr.storyapp.data.source.remote.response.StoriesResponse
import com.atharianr.storyapp.data.source.remote.response.StoryDetailResponse
import com.atharianr.storyapp.data.source.remote.response.vo.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {
    fun getStory(): LiveData<PagingData<StoryEntity>> {
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

    fun getAllStories(bearerToken: String, location: Int): LiveData<ApiResponse<StoriesResponse>> {
        val resultResponse = MutableLiveData<ApiResponse<StoriesResponse>>()

        apiService.getAllStories(bearerToken, location).enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>, response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful) {
                    resultResponse.postValue(ApiResponse.success(response.body()))
                } else {
                    try {
                        val errorBody = response.errorBody()
                        if (errorBody != null) {
                            val jObjError = JSONObject(errorBody.string())
                            resultResponse.postValue(ApiResponse.error(jObjError.getString("message")))
                        }
                    } catch (e: Exception) {
                        resultResponse.postValue(ApiResponse.error(e.message))
                    }
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                resultResponse.postValue(ApiResponse.error(t.message))
            }
        })

        return resultResponse
    }

    fun getStoryDetail(
        bearerToken: String, id: String
    ): LiveData<ApiResponse<StoryDetailResponse>> {
        val resultResponse = MutableLiveData<ApiResponse<StoryDetailResponse>>()

        apiService.getStoryDetail(bearerToken, id).enqueue(object : Callback<StoryDetailResponse> {
            override fun onResponse(
                call: Call<StoryDetailResponse>, response: Response<StoryDetailResponse>
            ) {
                if (response.isSuccessful) {
                    resultResponse.postValue(ApiResponse.success(response.body()))
                } else {
                    try {
                        val errorBody = response.errorBody()
                        if (errorBody != null) {
                            val jObjError = JSONObject(errorBody.string())
                            resultResponse.postValue(ApiResponse.error(jObjError.getString("message")))
                        }
                    } catch (e: Exception) {
                        resultResponse.postValue(ApiResponse.error(e.message))
                    }
                }
            }

            override fun onFailure(call: Call<StoryDetailResponse>, t: Throwable) {
                resultResponse.postValue(ApiResponse.error(t.message))
            }
        })

        return resultResponse
    }

    fun addNewStory(
        bearerToken: String, image: MultipartBody.Part, desc: RequestBody
    ): LiveData<ApiResponse<AddStoryResponse>> {
        val resultResponse = MutableLiveData<ApiResponse<AddStoryResponse>>()

        apiService.addNewStory(bearerToken, image, desc)
            .enqueue(object : Callback<AddStoryResponse> {
                override fun onResponse(
                    call: Call<AddStoryResponse>, response: Response<AddStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        resultResponse.postValue(ApiResponse.success(response.body()))
                    } else {
                        try {
                            val errorBody = response.errorBody()
                            if (errorBody != null) {
                                val jObjError = JSONObject(errorBody.string())
                                resultResponse.postValue(ApiResponse.error(jObjError.getString("message")))
                            }
                        } catch (e: Exception) {
                            resultResponse.postValue(ApiResponse.error(e.message))
                        }
                    }
                }

                override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                    resultResponse.postValue(ApiResponse.error(t.message))
                }
            })

        return resultResponse
    }
}