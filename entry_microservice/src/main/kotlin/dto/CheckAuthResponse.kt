package ru.itmo.dto

data class CheckAuthResponse(
    val username: String?,
    val success: Boolean,
)
