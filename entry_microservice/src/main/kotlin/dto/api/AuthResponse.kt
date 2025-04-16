package ru.itmo.dto.api

data class AuthResponse(
    val username: String,
    val token: String,
    val success: Boolean = true
)
