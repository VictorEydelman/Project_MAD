package ru.itmo.dto.keydb

data class UserRequest<T>(
    val username: String,
    val data: T
)
