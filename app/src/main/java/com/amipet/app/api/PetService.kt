package com.amipet.app.api

import com.amipet.app.model.Animal
import com.amipet.app.model.ApiResponse
import com.amipet.app.model.Match
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PetService {
    @GET("pets")
    suspend fun getAllPets(): ApiResponse<List<Animal>>
    
    @GET("pets/available")
    suspend fun getAvailablePets(): ApiResponse<List<Animal>>
    
    @GET("pets/{id}")
    suspend fun getPetById(@Path("id") id: String): ApiResponse<Animal>
    
    @POST("pets/{id}/like")
    suspend fun likePet(@Path("id") id: String): ApiResponse<Match>
    
    @POST("pets/{id}/dislike")
    suspend fun dislikePet(@Path("id") id: String): ApiResponse<Nothing>
    
    @GET("pets/search")
    suspend fun searchPets(
        @Query("species") species: String? = null,
        @Query("breed") breed: String? = null,
        @Query("age") age: String? = null,
        @Query("size") size: String? = null
    ): ApiResponse<List<Animal>>
}
