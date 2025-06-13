package com.amipet.app.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val phone: String? = null,
    val address: String? = null,
    val type: String = "ADOPTER"
)
