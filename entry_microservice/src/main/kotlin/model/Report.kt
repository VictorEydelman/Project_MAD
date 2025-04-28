package ru.itmo.model

import java.time.Duration
import java.time.LocalTime

/**
 * Объект отчёта за период для построения статистики
 * @param quality Качество сна (от 0 до 100)
 * @param startTime Время, когда пользователь уснул первый раз за период
 * @param endTime Время, когда пользователь проснулся последний раз за период
 * @param totalSleep Суммарное время нахождения во сне
 * @param awakenings Среднее количество пробуждений во время сессии сна (без учета последнего окончательного пробуждения)
 * @param avgAwake Среднее время бодрствований внутри сессии сна (от первого засыпания до окончательного пробуждения)
 * @param avgAsleep Среднее время сессий сна
 * @param avgToFallAsleep Среднее время, понадобившееся пользователю чтобы заснуть
 * @param data Подробные данные сна за период. Нужны только для дневного (daily) отчёта
 * @param distribution Распределение времени сна по дням за неделю. Нужно только для недельного (weekly) отчёта
 */
data class Report(
    val quality: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val totalSleep: Duration,
    val awakenings: Int,
    val avgAwake: Duration,
    val avgAsleep: Duration,
    val avgToFallAsleep: Duration,
    val data: SleepData?,
    val distribution: WeekdaySleepDistribution?,
)
