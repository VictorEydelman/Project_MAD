package ru.itmo

import kotlinx.serialization.Serializable

@Serializable
data class RequestTemplate(
    val address: String,
    val method: String,
    val body: String,
    val args: List<String>
)