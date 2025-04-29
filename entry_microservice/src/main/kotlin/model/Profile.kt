package ru.itmo.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDate
import java.time.LocalTime

enum class Gender { Male, Female, Null }
enum class Periodicity { OnceADay, TwiceADay, ThreeTimesADay, OnceEveryTwoDays, TwiceAWeek, ThreeTimesAWeek, Rarely, Often, Never, Null }

/**
 * Объект будильника для сохранения
 * @param time Время будильника
 * @param alarm Активен ли будильник
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Alarm(val time: LocalTime, val alarm: Boolean)

/**
 * Объект рекомендуемого отхода ко сну для сохранения
 * @param time Время отхода ко сну
 * @param remindMeToSleep Нужно ли уведомление в это время
 * @param remindBeforeBad Нужно ли уведомление за 15 минут до этого времени
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class BedTime(val time: LocalTime, val remindMeToSleep: Boolean, val remindBeforeBad: Boolean)


/**
 * Объект профиля (настроек) пользователя
 * @param name Имя
 * @param surname Фамилия
 * @param birthday Дата рождения
 * @param gender Пол
 * @param physicalCondition Периодичность занятий спортом
 * @param caffeineUsage Переодичность потребления кофеина
 * @param alcoholUsage Переодичность потребления алкоголя
 * @param alarmRecurring Постоянный повторяющийся будильник
 * @param alarmTemporary Временный сбрасывающийся после звонка будильник
 * @param bedTimeRecurring Заданное постоянное желаемое время отхода ко сну
 * @param bedTimeTemporary Временное сбрасывающееся после уведомления отхода ко сну
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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
