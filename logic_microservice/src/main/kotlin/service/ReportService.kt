package ru.itmo.service
import KeyDBClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.itmo.model.*
import java.time.*

class ReportService(private val keydb: KeyDBClient) {

    suspend fun makeDailyReport(username: String): Report = withContext(Dispatchers.IO) {
        val now = LocalDateTime.now()
        val start = now.minusDays(1).withHour(0).withMinute(0)
        val end = now.withHour(23).withMinute(59)

        val stats = getSleepStatistics(username, start, end)

        Report(
            quality = calculateQuality(stats),
            startTime = calculateStartTime(stats),
            endTime = calculateEndTime(stats),
            totalSleep = calculateTotalSleep(stats),
            awakenings = calculateAwakenings(stats),
            avgAwake = calculateAvgAwake(stats),
            avgAsleep = calculateAvgAsleep(stats),
            avgToFallAsleep = calculateAvgToFallAsleep(stats),
            data = stats,
            distribution = null
        )
    }

    suspend fun makeWeeklyReport(username: String): Report = withContext(Dispatchers.IO) {
        val now = LocalDateTime.now()
        val start = now.minusWeeks(1).withHour(0).withMinute(0)
        val end = now.withHour(23).withMinute(59)

        val stats = getSleepStatistics(username, start, end)

        Report(
            quality = calculateQuality(stats),
            startTime = calculateStartTime(stats),
            endTime = calculateEndTime(stats),
            totalSleep = calculateTotalSleep(stats),
            awakenings = calculateAwakenings(stats),
            avgAwake = calculateAvgAwake(stats),
            avgAsleep = calculateAvgAsleep(stats),
            avgToFallAsleep = calculateAvgToFallAsleep(stats),
            data = null,
            distribution = calculateWeekdayDistribution(stats)
        )
    }

    suspend fun makeAllTimeReport(username: String): Report = withContext(Dispatchers.IO) {
        val stats = getSleepStatistics(username, LocalDateTime.MIN, LocalDateTime.MAX)

        Report(
            quality = calculateQuality(stats),
            startTime = calculateStartTime(stats),
            endTime = calculateEndTime(stats),
            totalSleep = calculateTotalSleep(stats),
            awakenings = calculateAwakenings(stats),
            avgAwake = calculateAvgAwake(stats),
            avgAsleep = calculateAvgAsleep(stats),
            avgToFallAsleep = calculateAvgToFallAsleep(stats),
            data = null,
            distribution = calculateWeekdayDistribution(stats)
        )
    }

    private suspend fun getSleepStatistics(username: String, start: LocalDateTime, end: LocalDateTime): SleepData {
        val response: SleepData = keydb.sendRequest(
            channel = "get-sleepStatistic-Interval",
            message = SleepInterval(username, start, end)
        )!!
        return response
//        return response.map { map ->
//            SleepDataPiece(
//                timestamp = LocalDateTime.parse(map["timestamp"] as String),
//                pulse = (map["pulse"] as Double).toInt(),
//                sleepPhase = SleepPhase.valueOf(map["sleepPhase"] as String)
//            )
//        }
    }

    private fun calculateQuality(data: SleepData): Int {
        if (data.isEmpty()) return 0

        val totalPhases = data.size
        val deepSleep = data.count { it.sleepPhase == SleepPhase.DEEP }
        val remSleep = data.count { it.sleepPhase == SleepPhase.REM }
        val awake = data.count { it.sleepPhase == SleepPhase.AWAKE }
        val lightSleep = data.count { it.sleepPhase == SleepPhase.LIGHT }
        val drowsySleep = data.count { it.sleepPhase == SleepPhase.DROWSY }

        // Формула расчета качества сна
        return (
                (deepSleep * 1.5) +
                        (remSleep * 1.3) +
                        (lightSleep * 1.0) +
                        (drowsySleep * 0.5) +
                        (awake * -0.5)
                ).let { weightedSum -> ((weightedSum / totalPhases) * 100).toInt().coerceIn(0, 100) }
    }

    private fun calculateStartTime(data: SleepData): LocalTime {
        if (data.isEmpty()) return LocalTime.MIDNIGHT

        // Находим первую фазу сна (не бодрствования)
        val firstSleep = data.firstOrNull { it.sleepPhase != SleepPhase.AWAKE }
            ?: return data.minByOrNull { it.timestamp }?.timestamp?.toLocalTime() ?: LocalTime.MIDNIGHT

        // Ищем начало за 30 минут до первой фазы сна
        val startTime = data.filter {
            it.timestamp.isBefore(firstSleep.timestamp) &&
                    it.timestamp.isAfter(firstSleep.timestamp.minusMinutes(30))
        }.minByOrNull { it.timestamp }?.timestamp?.toLocalTime()

        return startTime ?: firstSleep.timestamp.toLocalTime()
    }

    private fun calculateEndTime(data: SleepData): LocalTime {
        if (data.isEmpty()) return LocalTime.MIDNIGHT

        // Находим последнюю фазу сна (не бодрствования)
        val lastSleep = data.lastOrNull { it.sleepPhase != SleepPhase.AWAKE }
            ?: return data.maxByOrNull { it.timestamp }?.timestamp?.toLocalTime() ?: LocalTime.MIDNIGHT

        // Ищем конец через 30 минут после последней фазы сна
        val endTime = data.filter {
            it.timestamp.isAfter(lastSleep.timestamp) &&
                    it.timestamp.isBefore(lastSleep.timestamp.plusMinutes(30))
        }.maxByOrNull { it.timestamp }?.timestamp?.toLocalTime()

        return endTime ?: lastSleep.timestamp.toLocalTime()
    }

    private fun calculateTotalSleep(data: SleepData): Duration {
        if (data.isEmpty()) return Duration.ZERO

        val asleepPhases = listOf(SleepPhase.REM, SleepPhase.LIGHT, SleepPhase.DEEP, SleepPhase.DROWSY)
        val sleepPeriods = mutableListOf<Duration>()
        var sleepStart: LocalDateTime? = null

        // Группируем данные по дням
        val byDays = data.groupBy { it.timestamp.toLocalDate() }

        for ((_, dayData) in byDays) {
            val sorted = dayData.sortedBy { it.timestamp }

            for (i in sorted.indices) {
                val current = sorted[i]

                if (current.sleepPhase in asleepPhases && sleepStart == null) {
                    sleepStart = current.timestamp
                } else if (current.sleepPhase == SleepPhase.AWAKE && sleepStart != null) {
                    sleepPeriods.add(Duration.between(sleepStart, current.timestamp))
                    sleepStart = null
                }
            }

            // Добавляем последний период сна, если он не закончился бодрствованием
            if (sleepStart != null) {
                sleepPeriods.add(Duration.between(sleepStart, sorted.last().timestamp))
                sleepStart = null
            }
        }

        return sleepPeriods.fold(Duration.ZERO) { acc, duration -> acc + duration }
    }

    private fun calculateAwakenings(data: SleepData): Int {
        if (data.size < 2) return 0

        val asleepPhases = listOf(SleepPhase.REM, SleepPhase.LIGHT, SleepPhase.DEEP)
        var awakenings = 0
        var wasAsleep = false

        for (i in 1 until data.size) {
            val prev = data[i-1]
            val current = data[i]

            if (prev.sleepPhase in asleepPhases && current.sleepPhase == SleepPhase.AWAKE) {
                if (wasAsleep) {
                    awakenings++
                }
                wasAsleep = false
            } else if (current.sleepPhase in asleepPhases) {
                wasAsleep = true
            }
        }

        return awakenings
    }

    private fun calculateAvgAwake(data: SleepData): Duration {
        if (data.isEmpty()) return Duration.ZERO

        val awakePeriods = mutableListOf<Duration>()
        var awakeStart: LocalDateTime? = null

        for (i in 1 until data.size) {
            val prev = data[i-1]
            val current = data[i]

            if (prev.sleepPhase != SleepPhase.AWAKE && current.sleepPhase == SleepPhase.AWAKE) {
                awakeStart = current.timestamp
            } else if (prev.sleepPhase == SleepPhase.AWAKE && current.sleepPhase != SleepPhase.AWAKE && awakeStart != null) {
                awakePeriods.add(Duration.between(awakeStart, current.timestamp))
                awakeStart = null
            }
        }

        if (awakePeriods.isEmpty()) return Duration.ZERO
        val awake = awakePeriods.fold(Duration.ZERO) { acc, duration -> acc + duration }
        return awake.dividedBy(awakePeriods.size.toLong())
    }

    private fun calculateAvgAsleep(data: SleepData): Duration {
        val totalSleep = calculateTotalSleep(data)
        if (totalSleep == Duration.ZERO) return Duration.ZERO

        val days = data.map { it.timestamp.toLocalDate() }.distinct().size
        return totalSleep.dividedBy(days.toLong())
    }

    private fun calculateAvgToFallAsleep(data: SleepData): Duration {
        if (data.isEmpty()) return Duration.ZERO

        val asleepPhases = listOf(SleepPhase.REM, SleepPhase.LIGHT, SleepPhase.DEEP)
        val fallTimes = mutableListOf<Duration>()
        var sleepStart: LocalDateTime? = null

        val byDays = data.groupBy { it.timestamp.toLocalDate() }

        for ((_, dayData) in byDays) {
            val sorted = dayData.sortedBy { it.timestamp }

            for (i in sorted.indices) {
                val current = sorted[i]

                if (current.sleepPhase == SleepPhase.AWAKE && sleepStart == null) {
                    sleepStart = current.timestamp
                } else if (current.sleepPhase in asleepPhases && sleepStart != null) {
                    fallTimes.add(Duration.between(sleepStart, current.timestamp))
                    sleepStart = null
                }
            }
        }

        if (fallTimes.isEmpty()) return Duration.ZERO
        val total = fallTimes.fold(Duration.ZERO) { acc, duration -> acc + duration }
        return total.dividedBy(fallTimes.size.toLong())
    }

    private fun calculateWeekdayDistribution(data: SleepData): WeekdaySleepDistribution {
        if (data.isEmpty()) return emptyList()

        val byWeekday = data.groupBy {
            Weekday.valueOf(it.timestamp.dayOfWeek.name)
        }

        return Weekday.entries.map { weekday ->
            val dayData = byWeekday[weekday] ?: emptyList()
            val totalSleep = calculateTotalSleep(dayData).toHours().toDouble()

            WeekdaySleep(
                weekday = weekday,
                asleepHours = totalSleep
            )
        }
    }
}