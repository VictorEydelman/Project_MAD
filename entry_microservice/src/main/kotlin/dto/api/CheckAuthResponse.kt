package ru.itmo.dto.api

data class CheckAuthResponse(
    val username: String?,
    val success: Boolean,
)
