package ru.itmo.keydb

data class KeyDBRequest<T>(
    val action: String,
    val payload: T?,
)
