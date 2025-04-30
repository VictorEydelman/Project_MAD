package org.example.dto

data class ApiResponse<T>(
        val success: Boolean,
        val message: String? = null,
        val data: T? = null
)
