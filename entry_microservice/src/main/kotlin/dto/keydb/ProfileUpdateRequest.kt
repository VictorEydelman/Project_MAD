package ru.itmo.dto.keydb

import ru.itmo.model.Profile

data class ProfileUpdateRequest(
    val username: String,
    val profile: Profile
)
