package com.amipet.app.repository

import com.amipet.app.api.ApiClient
import com.amipet.app.model.ApiResponse
import com.amipet.app.model.Message

class MessageRepository(private val apiClient: ApiClient) {

    suspend fun getAllMessages(): ApiResponse<List<Message>> {
        // Since ApiClient doesn't have a getAllMessages method, we'll assume this needs to be added
        throw NotImplementedError("getAllMessages is not implemented in ApiClient")
    }

    suspend fun getMessagesByMatchId(matchId: String): ApiResponse<List<Message>> {
        return apiClient.getMessagesByMatchId(matchId)
    }

    suspend fun getMessagesByUserId(userId: String): ApiResponse<List<Message>> {
        // Since ApiClient doesn't have a getMessagesByUserId method, we'll assume this needs to be added
        throw NotImplementedError("getMessagesByUserId is not implemented in ApiClient")
    }

    suspend fun sendMessage(message: Message): ApiResponse<Message> {
        return apiClient.sendMessage(message)
    }

    suspend fun markAsRead(id: String): ApiResponse<Nothing> {
        // Since ApiClient doesn't have a markAsRead method, we'll assume this needs to be added
        throw NotImplementedError("markAsRead is not implemented in ApiClient")
    }
}