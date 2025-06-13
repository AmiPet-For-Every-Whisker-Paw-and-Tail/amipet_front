package com.amipet.app.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String? = null,
    val address: String? = null,
    val profileImageUrl: String? = null,
    val type: UserType = UserType.ADOPTER
)

enum class UserType {
    ADOPTER,
    DONOR,
    ADMIN
}
