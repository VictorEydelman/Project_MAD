package ru.itmo.dto.keydb

import ru.itmo.model.SleepData

data class SleepUploadRequest(
    val username: String,
    val data: SleepData
)
