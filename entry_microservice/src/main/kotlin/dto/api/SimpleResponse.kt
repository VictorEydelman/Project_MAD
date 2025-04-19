package ru.itmo.dto.api

data class SimpleResponse(
    val success: Boolean,
    val message: String?
) {
    companion object {
        fun success(message: String? = null): SimpleResponse {
            return SimpleResponse(success = true, message = message)
        }

        fun failure(message: String? = null): SimpleResponse {
            return SimpleResponse(success = false, message = message)
        }
    }
}
