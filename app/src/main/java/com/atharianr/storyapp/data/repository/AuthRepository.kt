package com.atharianr.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.atharianr.storyapp.data.source.remote.network.ApiService
import com.atharianr.storyapp.data.source.remote.request.LoginRequest
import com.atharianr.storyapp.data.source.remote.request.RegisterRequest
import com.atharianr.storyapp.data.source.remote.response.AuthResponse
import com.atharianr.storyapp.data.source.remote.response.vo.ApiResponse

class AuthRepository(private val apiService: ApiService) {
    fun register(registerRequest: RegisterRequest): LiveData<ApiResponse<AuthResponse>> = liveData {
        emit(ApiResponse.loading())
        try {
            val response = apiService.register(registerRequest)
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

    fun login(loginRequest: LoginRequest): LiveData<ApiResponse<AuthResponse>> = liveData {
        emit(ApiResponse.loading())
        try {
            val response = apiService.login(loginRequest)
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