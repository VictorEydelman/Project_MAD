package ru.itmo.model

import java.time.LocalDate
import java.time.LocalTime

enum class Gender { Male, Female, Null }
enum class Periodicity { OnceADay, TwiceADay, ThreeTimesADay, OnceEveryTwoDays, TwiceAWeek, ThreeTimesAWeek, Rarely, Often, Never, Null }

data class Profile(
    val birth_date: LocalDate?,
    val gender: Gender?,
    val physical_activity: Periodicity?,
    val caffeine_consumption: Periodicity?,
    val alcohol_consumption: Periodicity?,
    val asleep_time: LocalTime?,
    val awake_time: LocalTime?,
)
