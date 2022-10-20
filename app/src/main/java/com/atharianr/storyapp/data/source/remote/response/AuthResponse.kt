package com.atharianr.storyapp.data.source.remote.response

data class AuthResponse(
    val error: Boolean = false,
    val message: String = "",
    val loginResult: LoginResult = LoginResult()
) {
    data class LoginResult(
        val userId: String = "",
        val name: String = "",
        val token: String = ""
    )
}