package ru.itmo.dto

data class AuthRespose(
    val username: String,
    val token: String,
    val success: Boolean = true
)
