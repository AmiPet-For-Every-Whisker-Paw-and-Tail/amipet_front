package com.amipet.app.mock

import com.amipet.app.local.LocalDataProvider
import com.amipet.app.model.Animal
import com.amipet.app.model.ApiResponse
import com.amipet.app.model.Match
import com.amipet.app.model.MatchStatus
import com.amipet.app.model.Message
import com.amipet.app.model.User
import com.amipet.app.model.UserType
import kotlinx.coroutines.delay

/**
 * Class that simulates API calls to the backend in memory.
 * Uses the LocalDataProvider to provide sample data.
 */
class MockApiService {

    // Simulate network delay
    private val networkDelay = 500L

    // Authentication
    suspend fun login(email: String, password: String): ApiResponse<Map<String, String>> {
        delay(networkDelay)

        // Handle admin login
        if (email == "admin" && password == "admin") {
            return ApiResponse(
                success = true,
                data = mapOf(
                    "token" to "admin_mock_token",
                    "userId" to "admin_user_id"
                )
            )
        }

        // Handle regular user login
        val user = LocalDataProvider.users.find { it.email == email }

        return if (user != null && password == "123456") {
            ApiResponse(
                success = true,
                data = mapOf(
                    "token" to "mock_token_${user.id}",
                    "userId" to user.id
                )
            )
        } else {
            ApiResponse(
                success = false,
                error = "Invalid email or password"
            )
        }
    }

    suspend fun register(name: String, email: String, password: String): ApiResponse<User> {
        delay(networkDelay)

        val existingUser = LocalDataProvider.users.find { it.email == email }

        return if (existingUser == null) {
            ApiResponse(
                success = true,
                data = LocalDataProvider.currentUser
            )
        } else {
            ApiResponse(
                success = false,
                error = "Email already registered"
            )
        }
    }

    suspend fun forgotPassword(email: String): ApiResponse<Unit> {
        delay(networkDelay)

        val user = LocalDataProvider.users.find { it.email == email }

        return if (user != null) {
            ApiResponse(success = true) // Simulate successful email sending
        } else {
            ApiResponse(success = false, error = "Email not found")
        }
    }

    suspend fun logout(): ApiResponse<Nothing> {
        delay(networkDelay)
        return ApiResponse(success = true)
    }

    // Users
    suspend fun getCurrentUser(): ApiResponse<User> {
        delay(networkDelay)
        return ApiResponse(
            success = true,
            data = LocalDataProvider.currentUser
        )
    }

    suspend fun getUserById(id: String): ApiResponse<User> {
        delay(networkDelay)

        val user = LocalDataProvider.users.find { it.id == id }

        return if (user != null) {
            ApiResponse(
                success = true,
                data = user
            )
        } else {
            ApiResponse(
                success = false,
                error = "User not found"
            )
        }
    }

    suspend fun updateProfile(user: User): ApiResponse<User> {
        delay(networkDelay)

        val existingUser = LocalDataProvider.users.find { it.id == user.id }

        return if (existingUser != null) {
            // Simulate updating the user in the local data
            val updatedUser = user.copy() // Create a copy to simulate persistence
            ApiResponse(
                success = true,
                data = updatedUser
            )
        } else {
            ApiResponse(
                success = false,
                error = "User not found"
            )
        }
    }

    // Pets
    suspend fun getAllPets(): ApiResponse<List<Animal>> {
        delay(networkDelay)
        return ApiResponse(
            success = true,
            data = LocalDataProvider.pets
        )
    }

    suspend fun getAvailablePets(): ApiResponse<List<Animal>> {
        delay(networkDelay)
        return ApiResponse(
            success = true,
            data = LocalDataProvider.pets.filter { !it.isAdopted }
        )
    }

    suspend fun getPetById(id: String): ApiResponse<Animal> {
        delay(networkDelay)

        val pet = LocalDataProvider.pets.find { it.id == id }

        return if (pet != null) {
            ApiResponse(
                success = true,
                data = pet
            )
        } else {
            ApiResponse(
                success = false,
                error = "Pet not found"
            )
        }
    }

    suspend fun likePet(id: String): ApiResponse<Match> {
        delay(networkDelay)

        val pet = LocalDataProvider.pets.find { it.id == id }

        return if (pet != null) {
            val match = Match(
                id = "match_${System.currentTimeMillis()}",
                userId = LocalDataProvider.currentUser.id,
                animalId = pet.id,
                timestamp = System.currentTimeMillis(),
                status = MatchStatus.PENDING
            )

            ApiResponse(
                success = true,
                data = match
            )
        } else {
            ApiResponse(
                success = false,
                error = "Pet not found"
            )
        }
    }

    suspend fun dislikePet(id: String): ApiResponse<Nothing> {
        delay(networkDelay)
        return ApiResponse(success = true)
    }

    // Matches
    suspend fun getMatches(): ApiResponse<List<Match>> {
        delay(networkDelay)
        return ApiResponse(
            success = true,
            data = LocalDataProvider.matches
        )
    }

    // Messages
    suspend fun getMessagesByMatchId(matchId: String): ApiResponse<List<Message>> {
        delay(networkDelay)
        return ApiResponse(
            success = true,
            data = LocalDataProvider.messages.filter { it.matchId == matchId }
        )
    }

    suspend fun sendMessage(message: Message): ApiResponse<Message> {
        delay(networkDelay)

        val newMessage = message.copy(
            id = "msg_${System.currentTimeMillis()}",
            timestamp = System.currentTimeMillis()
        )

        return ApiResponse(
            success = true,
            data = newMessage
        )
    }

    // Search Pets
    suspend fun searchPets(
        species: String? = null,
        breed: String? = null,
        age: String? = null,
        size: String? = null
    ): ApiResponse<List<Animal>> {
        delay(networkDelay)

        var filteredPets = LocalDataProvider.pets

        species?.let { s ->
            filteredPets = filteredPets.filter { it.species.equals(s, ignoreCase = true) }
        }
        breed?.let { b ->
            filteredPets = filteredPets.filter { it.breed?.equals(b, ignoreCase = true) == true }
        }
        age?.let { a ->
            filteredPets = filteredPets.filter { it.age == a }
        }
        size?.let { sz ->
            filteredPets = filteredPets.filter { it.size?.equals(sz, ignoreCase = true) == true }
        }

        return ApiResponse(
            success = true,
            data = filteredPets
        )
    }
}