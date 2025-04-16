package ru.itmo.dto

data class AuthResponse(
    val username: String,
    val token: String,
    val success: Boolean = true
)
