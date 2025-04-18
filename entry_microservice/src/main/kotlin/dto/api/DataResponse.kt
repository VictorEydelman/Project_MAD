package ru.itmo.dto.api

data class DataResponse<T>(
    val data: T,
    val message: String? = null,
    val success: Boolean = true,
) {
    companion object {
        fun <T> of(data: T): DataResponse<T> = DataResponse(data)
    }
}
