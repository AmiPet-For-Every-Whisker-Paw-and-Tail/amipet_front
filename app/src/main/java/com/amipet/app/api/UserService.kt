package com.amipet.app.api

import com.amipet.app.model.ApiResponse
import com.amipet.app.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @GET("users/me")
    suspend fun getCurrentUser(): ApiResponse<User>
    
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): ApiResponse<User>
    
    @PUT("users/me")
    suspend fun updateProfile(@Body user: User): ApiResponse<User>
}
