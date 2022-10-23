package com.atharianr.storyapp.data.source.remote.network

import com.atharianr.storyapp.data.source.remote.request.LoginRequest
import com.atharianr.storyapp.data.source.remote.request.RegisterRequest
import com.atharianr.storyapp.data.source.remote.response.AddStoryResponse
import com.atharianr.storyapp.data.source.remote.response.AuthResponse
import com.atharianr.storyapp.data.source.remote.response.StoriesResponse
import com.atharianr.storyapp.data.source.remote.response.StoryDetailResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun register(@Body registerRequest: RegisterRequest): Call<AuthResponse>

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<AuthResponse>

    @GET("stories")
    fun getAllStories(@Header("Authorization") bearerToken: String): Call<StoriesResponse>

    @GET("stories/{id}")
    fun getStoryDetail(
        @Header("Authorization") bearerToken: String, @Path("id") id: String
    ): Call<StoryDetailResponse>

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") bearerToken: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<AddStoryResponse>
}