package com.atharianr.storyapp.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.atharianr.storyapp.data.source.remote.network.ApiService
import com.atharianr.storyapp.data.source.remote.request.LoginRequest
import com.atharianr.storyapp.data.source.remote.request.RegisterRequest
import com.atharianr.storyapp.data.source.remote.response.AuthResponse
import com.atharianr.storyapp.data.source.remote.response.vo.ApiResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource(private val apiService: ApiService) {
    fun register(registerRequest: RegisterRequest): LiveData<ApiResponse<AuthResponse>> {
        val resultResponse = MutableLiveData<ApiResponse<AuthResponse>>()

        apiService.register(registerRequest).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
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

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                resultResponse.postValue(ApiResponse.error(t.message))
            }
        })

        return resultResponse
    }

    fun login(loginRequest: LoginRequest): LiveData<ApiResponse<AuthResponse>> {
        val resultResponse = MutableLiveData<ApiResponse<AuthResponse>>()

        apiService.login(loginRequest).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
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

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                resultResponse.postValue(ApiResponse.error(t.message))
            }
        })

        return resultResponse
    }
}