package com.atharianr.storyapp.data.source.remote.response.vo

class ApiResponse<T>(val status: StatusResponse, val body: T?, val message: String?) {
    companion object {
        fun <T> success(body: T?): ApiResponse<T> = ApiResponse(StatusResponse.SUCCESS, body, null)
        fun <T : Any> loading(): ApiResponse<T> = ApiResponse(StatusResponse.LOADING, null, null)
        fun <T : Any> error(msg: String?): ApiResponse<T> =
            ApiResponse(StatusResponse.ERROR, null, msg)
    }
}