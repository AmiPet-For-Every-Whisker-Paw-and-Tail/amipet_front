package com.amipet.app.model

data class Message(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Long,
    val matchId: String? = null,
    val isRead: Boolean = false
)
