package com.atharianr.storyapp.data.source.remote.network

import com.atharianr.storyapp.data.source.remote.request.LoginRequest
import com.atharianr.storyapp.data.source.remote.request.RegisterRequest
import com.atharianr.storyapp.data.source.remote.response.AddStoryResponse
import com.atharianr.storyapp.data.source.remote.response.AuthResponse
import com.atharianr.storyapp.data.source.remote.response.StoriesResponse
import com.atharianr.storyapp.data.source.remote.response.StoryDetailResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest): AuthResponse

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): AuthResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") bearerToken: String, @Query("location") location: Int
    ): StoriesResponse

    @GET("stories")
    suspend fun getAllStoriesPaging(
        @Header("Authorization") bearerToken: String,
        @Query("location") location: Int,
        @Query("page") page: Int
    ): StoriesResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Header("Authorization") bearerToken: String, @Path("id") id: String
    ): StoryDetailResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") bearerToken: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): AddStoryResponse
}