package com.amipet.app.model

data class Animal(
    val id: String,
    val name: String,
    val species: String,
    val breed: String?,
    val age: String,
    val gender: String,
    val size: String,
    val description: String,
    val imageUrl: String,
    val ownerId: String,
    val isAdopted: Boolean = false
)
