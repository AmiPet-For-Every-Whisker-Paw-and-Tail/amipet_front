package com.amipet.app.api

import com.amipet.app.model.ApiResponse
import com.amipet.app.model.Message
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MessageService {
    @GET("messages")
    suspend fun getAllMessages(): ApiResponse<List<Message>>
    
    @GET("messages/match/{matchId}")
    suspend fun getMessagesByMatchId(@Path("matchId") matchId: String): ApiResponse<List<Message>>
    
    @GET("messages/user/{userId}")
    suspend fun getMessagesByUserId(@Path("userId") userId: String): ApiResponse<List<Message>>
    
    @POST("messages/send")
    suspend fun sendMessage(@Body message: Message): ApiResponse<Message>
    
    @POST("messages/{id}/read")
    suspend fun markAsRead(@Path("id") id: String): ApiResponse<Nothing>
}
