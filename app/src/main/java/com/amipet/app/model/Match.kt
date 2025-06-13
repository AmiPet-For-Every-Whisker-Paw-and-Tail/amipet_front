package com.amipet.app.model

enum class MatchStatus {
    PENDING,
    APPROVED,
    REJECTED
}

data class Match(
    val id: String,
    val userId: String,
    val animalId: String,
    val timestamp: Long,
    val status: MatchStatus
)