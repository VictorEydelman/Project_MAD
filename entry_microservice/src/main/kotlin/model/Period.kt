package ru.itmo.model

import java.time.Instant

data class Period(
    val from: Instant,
    val to: Instant,
)
