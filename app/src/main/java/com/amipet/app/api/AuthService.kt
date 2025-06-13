package com.amipet.app.api

import com.amipet.app.model.ApiResponse
import com.amipet.app.model.LoginRequest
import com.amipet.app.model.RegisterRequest
import com.amipet.app.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResponse<Map<String, String>>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): ApiResponse<User>

    @POST("auth/logout")
    suspend fun logout(): ApiResponse<Nothing>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body email: Map<String, String>): ApiResponse<Unit>
}