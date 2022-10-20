package com.atharianr.storyapp.data.source.remote.network

import com.atharianr.storyapp.data.source.remote.request.LoginRequest
import com.atharianr.storyapp.data.source.remote.request.RegisterRequest
import com.atharianr.storyapp.data.source.remote.response.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    fun register(@Body registerRequest: RegisterRequest): Call<AuthResponse>

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<AuthResponse>
}