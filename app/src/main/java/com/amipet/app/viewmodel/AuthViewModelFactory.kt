package com.amipet.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amipet.app.api.ApiClient
import com.amipet.app.repository.AuthRepository
import com.amipet.app.repository.UserRepository
import com.amipet.app.util.SessionManager
import java.lang.IllegalArgumentException

class AuthViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val sessionManager = SessionManager(context)
        val apiClient = ApiClient(sessionManager)
        val authRepository = AuthRepository(
            apiClient,
            sessionManager = sessionManager
        )
        val userRepository = UserRepository(apiClient)
        println("Creating AuthViewModel with sessionManager: $sessionManager, authRepository: $authRepository, userRepository: $userRepository")
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository, userRepository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}