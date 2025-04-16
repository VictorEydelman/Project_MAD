package ru.itmo.dto.api

import ru.itmo.model.Profile

data class ProfileResponse(
    val profile: Profile,
    val success: Boolean = true,
)