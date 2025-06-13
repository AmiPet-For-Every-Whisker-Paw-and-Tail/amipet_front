package com.amipet.app.repository

import com.amipet.app.api.ApiClient
import com.amipet.app.model.ApiResponse
import com.amipet.app.model.RegisterRequest
import com.amipet.app.model.User
import com.amipet.app.util.SessionManager

class AuthRepository(
    private val apiClient: ApiClient,
    private val sessionManager: SessionManager
) {

    suspend fun login(email: String, password: String): ApiResponse<Map<String, String>> {
        val response = apiClient.login(email, password)

        if (response.success && response.data != null) {
            sessionManager.saveToken(response.data["token"] ?: "")
            sessionManager.saveUserId(response.data["userId"] ?: "")
        }

        return response
    }

    suspend fun register(
        name: String,
        email: String,
        password: String,
        phone: String? = null,
        address: String? = null
    ): ApiResponse<User> {
        // Use individual parameters as expected by ApiClient
        return apiClient.register(name, email, password) // Adjust based on ApiClient signature
    }

    suspend fun forgotPassword(email: String): ApiResponse<Unit> {
        return apiClient.forgotPassword(email) // Assuming ApiClient has this method
    }

    suspend fun logout(): ApiResponse<Nothing> {
        val response = apiClient.logout()
        sessionManager.clearSession()
        return response
    }

    fun isLoggedIn(): Boolean {
        return sessionManager.isLoggedIn()
    }
}