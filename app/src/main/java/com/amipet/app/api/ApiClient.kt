package com.amipet.app.api

import com.amipet.app.mock.MockApiService
import com.amipet.app.model.ApiResponse
import com.amipet.app.model.Animal
import com.amipet.app.model.Match
import com.amipet.app.model.Message
import com.amipet.app.model.User
import com.amipet.app.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * API Client that manages calls to the backend.
 * Supports both the real backend and a mock for testing and development.
 */
class ApiClient(private val sessionManager: SessionManager) {

    // Configuration to use the mock backend instead of the real backend
    private val useMockApi = true

    // Mock service for testing and development
    private val mockApiService = MockApiService()

    // Configuration of the Retrofit client for the real backend
    private val baseUrl = "http://10.0.2.2:3000/api/"

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = sessionManager.getToken()

        val requestBuilder = originalRequest.newBuilder()

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        chain.proceed(requestBuilder.build())
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val authService: AuthService = retrofit.create(AuthService::class.java)
    private val petService: PetService = retrofit.create(PetService::class.java)
    private val userService: UserService = retrofit.create(UserService::class.java)
    private val messageService: MessageService = retrofit.create(MessageService::class.java)

    // Authentication methods
    suspend fun login(email: String, password: String): ApiResponse<Map<String, String>> {
        return withContext(Dispatchers.IO) {
            if (useMockApi) {
                mockApiService.login(email, password)
            } else {
                try {
                    val response = authService.login(com.amipet.app.model.LoginRequest(email, password))
                    if (response.success && response.data != null) {
                        // Ensure data is a Map<String, String>
                        @Suppress("UNCHECKED_CAST")
                        val data = response.data as? Map<String, String> ?: emptyMap<String, String>()
                        ApiResponse(success = true, data = data, error = null)
                    } else {
                        ApiResponse(success = false, error = response.error ?: "Login failed")
                    }
                } catch (e: Exception) {
                    ApiResponse(success = false, error = e.message ?: "Unknown error")
                }
            }
        }
    }

    suspend fun register(name: String, email: String, password: String): ApiResponse<User> {
        return withContext(Dispatchers.IO) {
            if (useMockApi) {
                mockApiService.register(name, email, password)
            } else {
                try {
                    authService.register(com.amipet.app.model.RegisterRequest(name, email, password))
                } catch (e: Exception) {
                    ApiResponse(success = false, error = e.message)
                }
            }
        }
    }

    suspend fun forgotPassword(email: String): ApiResponse<Unit> {
        return withContext(Dispatchers.IO) {
            if (useMockApi) {
                mockApiService.forgotPassword(email)
            } else {
                try {
                    authService.forgotPassword(mapOf("email" to email)) // Match the @Body type in AuthService
                } catch (e: Exception) {
                    ApiResponse(success = false, error = e.message)
                }
            }
        }
    }

    suspend fun logout(): ApiResponse<Nothing> {
        return withContext(Dispatchers.IO) {
            if (useMockApi) {
                mockApiService.logout()
            } else {
                try {
                    authService.logout()
                } catch (e: Exception) {
                    ApiResponse(success = false, error = e.message)
                }
            }
        }
    }

    // User methods
    suspend fun getCurrentUser(): ApiResponse<User> {
        return withContext(Dispatchers.IO) {
            if (useMockApi) {
                mockApiService.getCurrentUser()
            } else {
                try {
                    userService.getCurrentUser()
                } catch (e: Exception) {
                    ApiResponse(success = false, error = e.message)
                }
            }
        }
    }

    suspend fun getUserById(id: String): ApiResponse<User> {
        return withContext(Dispatchers.IO) {
            if (useMockApi) {
                mockApiService.getUserById(id)
            } else {
                try {
                    userService.getUserById(id)
                } catch (e: Exception) {
                    ApiResponse(success = false, error = e.message)
                }
            }
        }
    }

    suspend fun updateProfile(user: User): ApiResponse<User> {
        return withContext(Dispatchers.IO) {
            if (useMockApi) {
                mockApiService.updateProfile(user)
            } else {
                try {
                    userService.updateProfile(user)
                } catch (e: Exception) {
                    ApiResponse(success = false, error = e.message)
                }
            }
        }
    }

    // Pet methods
    suspend fun getAllPets(): ApiResponse<List<Animal>> {
        return withContext(Dispatchers.IO) {
            if (useMockApi) {
                mockApiService.getAllPets()
            } else {
                try {
                    petService.getAllPets()
                } catch (e: Exception) {
                    ApiResponse(success = false, error = e.message)
                }
            }
        }
    }

    suspend fun getAvailablePets(): ApiResponse<List<Animal>> {
        return withContext(Dispatchers.IO) {
            if (useMockApi) {
                mockApiService.getAvailablePets()
            } else {
                try {
                    petService.getAvailablePets()
                } catch (e: Exception) {
                    ApiResponse(success = false, error = e.message)
                }
            }
        }
    }

    suspend fun getPetById(id: String): ApiResponse<Animal> {
        return withContext(Dispatchers.IO) {
            if (useMockApi) {
                mockApiService.getPetById(id)
            } else {
                try {
                    petService.getPetById(id)
                } catch (e: Exception) {
                    ApiResponse(success = false, error = e.message)
                }
            }
        }
    }

    suspend fun likePet(id: String): ApiResponse<Match> {
        return withContext(Dispatchers.IO) {
            if (useMockApi) {
                mockApiService.likePet(id)
            } else {
                try {
                    petService.likePet(id)
                } catch (e: Exception) {
                    ApiResponse(success = false, error = e.message)
                }
            }
        }
    }

    suspend fun dislikePet(id: String): ApiResponse<Nothing> {
        return withContext(Dispatchers.IO) {
            if (useMockApi) {
                mockApiService.dislikePet(id)
            } else {
                try {
                    petService.dislikePet(id)
                } catch (e: Exception) {
                    ApiResponse(success = false, error = e.message)
                }
            }
        }
    }

    suspend fun searchPets(
        species: String? = null,
        breed: String? = null,
        age: String? = null,
        size: String? = null
    ): ApiResponse<List<Animal>> {
        return withContext(Dispatchers.IO) {
            if (useMockApi) {
                mockApiService.searchPets(species, breed, age, size)
            } else {
                try {
                    petService.searchPets(species, breed, age, size)
                } catch (e: Exception) {
                    ApiResponse(success = false, error = e.message)
                }
            }
        }
    }

    // Message methods
    suspend fun getMessagesByMatchId(matchId: String): ApiResponse<List<Message>> {
        return withContext(Dispatchers.IO) {
            if (useMockApi) {
                mockApiService.getMessagesByMatchId(matchId)
            } else {
                try {
                    messageService.getMessagesByMatchId(matchId)
                } catch (e: Exception) {
                    ApiResponse(success = false, error = e.message)
                }
            }
        }
    }

    suspend fun sendMessage(message: Message): ApiResponse<Message> {
        return withContext(Dispatchers.IO) {
            if (useMockApi) {
                mockApiService.sendMessage(message)
            } else {
                try {
                    messageService.sendMessage(message)
                } catch (e: Exception) {
                    ApiResponse(success = false, error = e.message)
                }
            }
        }
    }
}