package com.amipet.app.repository

import com.amipet.app.api.ApiClient
import com.amipet.app.model.Animal
import com.amipet.app.model.ApiResponse
import com.amipet.app.model.Match

class PetRepository(private val apiClient: ApiClient) {

    suspend fun getAllPets(): ApiResponse<List<Animal>> {
        return apiClient.getAllPets()
    }

    suspend fun getAvailablePets(): ApiResponse<List<Animal>> {
        return apiClient.getAvailablePets()
    }

    suspend fun getPetById(id: String): ApiResponse<Animal> {
        return apiClient.getPetById(id)
    }

    suspend fun likePet(id: String): ApiResponse<Match> {
        return apiClient.likePet(id)
    }

    suspend fun dislikePet(id: String): ApiResponse<Nothing> {
        return apiClient.dislikePet(id)
    }

    suspend fun searchPets(
        species: String? = null,
        breed: String? = null,
        age: String? = null,
        size: String? = null
    ): ApiResponse<List<Animal>> {
        return apiClient.searchPets(species, breed, age, size)
    }
}