package ru.itmo.model

import java.time.LocalDate
import java.time.LocalTime

enum class Gender { male, female, Null }
enum class Periodicity { OnceADay, TwiceADay, ThreeTimesADay, OnceEveryTwoDays, TwiceAWeek, ThreeTimesAWeek, Rarely, Often, Never, Null }

data class Alarm(val time: LocalTime, val alarm: Boolean)
data class BedTime(val time: LocalTime, val remindMeToSleep: Boolean, val remindBeforeBad: Boolean)

data class Profile(
    val name: String,
    val surname: String,
    val birthday: LocalDate,
    val gender: Gender,
    val physicalCondition: Periodicity,
    val caffeineUsage: Periodicity,
    val alcoholUsage: Periodicity,
    val alarmRecurring: Alarm?,
    var alarmTemporary: Alarm?,
    val bedTimeRecurring: BedTime?,
    var bedTimeTemporary: BedTime?,
)
