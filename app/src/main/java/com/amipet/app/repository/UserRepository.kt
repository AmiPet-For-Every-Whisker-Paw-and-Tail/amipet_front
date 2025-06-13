package com.amipet.app.repository

import com.amipet.app.api.ApiClient
import com.amipet.app.model.ApiResponse
import com.amipet.app.model.User

class UserRepository(private val apiClient: ApiClient) {

    suspend fun getCurrentUser(): ApiResponse<User> {
        return apiClient.getCurrentUser()
    }

    suspend fun getUserById(id: String): ApiResponse<User> {
        return apiClient.getUserById(id)
    }

    suspend fun updateProfile(user: User): ApiResponse<User> {
        return apiClient.updateProfile(user)
    }
}