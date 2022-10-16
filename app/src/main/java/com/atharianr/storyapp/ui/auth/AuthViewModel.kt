package com.atharianr.storyapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.atharianr.storyapp.data.source.remote.RemoteDataSource
import com.atharianr.storyapp.data.source.remote.request.LoginRequest
import com.atharianr.storyapp.data.source.remote.request.RegisterRequest
import com.atharianr.storyapp.data.source.remote.response.BaseResponse
import com.atharianr.storyapp.data.source.remote.response.vo.ApiResponse

class AuthViewModel(private val remoteDataSource: RemoteDataSource) : ViewModel() {
    fun register(registerRequest: RegisterRequest): LiveData<ApiResponse<BaseResponse>> =
        remoteDataSource.register(registerRequest)

    fun login(loginRequest: LoginRequest): LiveData<ApiResponse<BaseResponse>> =
        remoteDataSource.login(loginRequest)
}