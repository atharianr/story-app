package com.atharianr.storyapp.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.atharianr.storyapp.data.source.remote.network.ApiService
import com.atharianr.storyapp.data.source.remote.request.LoginRequest
import com.atharianr.storyapp.data.source.remote.request.RegisterRequest
import com.atharianr.storyapp.data.source.remote.response.BaseResponse
import com.atharianr.storyapp.data.source.remote.response.vo.ApiResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource(private val apiService: ApiService) {
    fun register(registerRequest: RegisterRequest): LiveData<ApiResponse<BaseResponse>> {
        val resultResponse = MutableLiveData<ApiResponse<BaseResponse>>()

        apiService.register(registerRequest).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
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

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                resultResponse.postValue(ApiResponse.error(t.message))
            }
        })

        return resultResponse
    }

    fun login(loginRequest: LoginRequest): LiveData<ApiResponse<BaseResponse>> {
        val resultResponse = MutableLiveData<ApiResponse<BaseResponse>>()

        apiService.login(loginRequest).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
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

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                resultResponse.postValue(ApiResponse.error(t.message))
            }
        })

        return resultResponse
    }
}