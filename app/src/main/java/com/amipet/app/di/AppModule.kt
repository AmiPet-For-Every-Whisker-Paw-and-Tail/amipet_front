package com.amipet.app.di

import android.content.Context
import com.amipet.app.api.ApiClient
import com.amipet.app.mock.MockApiService
import com.amipet.app.repository.AuthRepository
import com.amipet.app.repository.MessageRepository
import com.amipet.app.repository.PetRepository
import com.amipet.app.repository.UserRepository
import com.amipet.app.util.SessionManager

/**
 * Classe responsável por fornecer as dependências para o aplicativo
 * Implementa um padrão de injeção de dependência simplificado
 */
object AppModule {
    
    // Configuração para usar o backend mock em vez do backend real
    private const val USE_MOCK_API = true
    
    // Instâncias únicas (Singleton)
    private var sessionManager: SessionManager? = null
    private var apiClient: ApiClient? = null
    private var mockApiService: MockApiService? = null
    
    // Repositórios
    private var authRepository: AuthRepository? = null
    private var petRepository: PetRepository? = null
    private var userRepository: UserRepository? = null
    private var messageRepository: MessageRepository? = null
    
    // Fornece o SessionManager
    fun provideSessionManager(context: Context): SessionManager {
        return sessionManager ?: SessionManager(context).also {
            sessionManager = it
        }
    }
    
    // Fornece o ApiClient
    fun provideApiClient(sessionManager: SessionManager): ApiClient {
        return apiClient ?: ApiClient(sessionManager).also {
            apiClient = it
        }
    }
    
    // Fornece o MockApiService
    fun provideMockApiService(): MockApiService {
        return mockApiService ?: MockApiService().also {
            mockApiService = it
        }
    }
    
    // Fornece o AuthRepository
    fun provideAuthRepository(context: Context): AuthRepository {
        return authRepository ?: run {
            val sessionManager = provideSessionManager(context)
            val apiClient = provideApiClient(sessionManager)
            AuthRepository(apiClient, sessionManager).also {
                authRepository = it
            }
        }
    }
    
    // Fornece o PetRepository
    fun providePetRepository(context: Context): PetRepository {
        return petRepository ?: run {
            val sessionManager = provideSessionManager(context)
            val apiClient = provideApiClient(sessionManager)
            PetRepository(apiClient).also {
                petRepository = it
            }
        }
    }
    
    // Fornece o UserRepository
    fun provideUserRepository(context: Context): UserRepository {
        return userRepository ?: run {
            val sessionManager = provideSessionManager(context)
            val apiClient = provideApiClient(sessionManager)
            UserRepository(apiClient).also {
                userRepository = it
            }
        }
    }
    
    // Fornece o MessageRepository
    fun provideMessageRepository(context: Context): MessageRepository {
        return messageRepository ?: run {
            val sessionManager = provideSessionManager(context)
            val apiClient = provideApiClient(sessionManager)
            MessageRepository(apiClient).also {
                messageRepository = it
            }
        }
    }
}
