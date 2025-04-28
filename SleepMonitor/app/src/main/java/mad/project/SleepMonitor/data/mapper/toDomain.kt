package mad.project.SleepMonitor.data.mapper

import mad.project.SleepMonitor.data.network.dto.ReportDataDto
import mad.project.SleepMonitor.data.network.dto.SleepDataPieceDto
import mad.project.SleepMonitor.domain.model.* // Импортируем все из domain.model
import java.time.* // Импортируем java.time
import java.time.format.DateTimeParseException
import kotlin.math.roundToInt

// --- Основной маппер DTO -> Domain ---

fun ReportDataDto.toDomain(): Report? { // Делаем возвращаемый тип nullable, если основные данные не пришли
    // Сначала мапим базовые поля
    val totalSleepDuration = this.totalSleepMillis?.let { Duration.ofMillis(it) }
    val awakeningsCount = this.awakenings
    val avgAwakeDuration = this.avgAwakeMillis?.let { Duration.ofMillis(it) }
    val avgAsleepDuration = this.avgAsleepMillis?.let { Duration.ofMillis(it) }

    // Мапим детальные данные сна, отфильтровывая некорректные записи
    val mappedSleepData: SleepData? = this.data
        ?.mapNotNull { it.toDomain() } // Используем mapNotNull с маппером для SleepDataPieceDto
        ?.sortedBy { it.timestamp } // Сортируем по времени на всякий случай

    // Если нет данных для анализа, возможно, стоит вернуть null
    if (mappedSleepData.isNullOrEmpty()) {
        // Можно вернуть Report с null/default значениями, если это допустимо
        println("Warning: No valid sleep data pieces found in the report.")
        // return null // Или вернуть отчет с пустыми/нулевыми значениями, если это нужно UI
    }

    // Вычисляем startTime и endTime из данных
    val reportStartTime: LocalTime? = mappedSleepData?.firstOrNull()?.timestamp?.atZone(ZoneId.systemDefault())?.toLocalTime()
    val reportEndTime: LocalTime? = mappedSleepData?.lastOrNull()?.timestamp?.atZone(ZoneId.systemDefault())?.toLocalTime()

    // Вычисляем качество сна (простая заглушка, нужна реальная логика)
    val calculatedQuality: Int = calculateSleepQuality(totalSleepDuration, awakeningsCount)

    // Вычисляем распределение по дням недели
    val calculatedDistribution: WeekdaySleepDistribution? = calculateWeekdayDistribution(mappedSleepData)

    // Собираем доменный объект Report
    // Поля, которые пришли напрямую или были только что вычислены/замаплены
    return Report(
        totalSleep = totalSleepDuration,
        awakenings = awakeningsCount,
        avgAwake = avgAwakeDuration,
        avgAsleep = avgAsleepDuration, // Это поле из DTO, не avgToFallAsleep
        data = mappedSleepData,
        // Поля, которые мы вычислили
        startTime = reportStartTime ?: LocalTime.MIDNIGHT, // Значение по умолчанию, если вычислить не удалось
        endTime = reportEndTime ?: LocalTime.MIDNIGHT,   // Значение по умолчанию
        quality = calculatedQuality,
        distribution = calculatedDistribution,
        // Поле, которое не можем вычислить из DTO - делаем nullable или ставим заглушку
        avgToFallAsleep = null // Сделали nullable в модели ИЛИ Duration.ZERO, если не nullable
    )
}

// --- Вспомогательные мапперы и функции ---

// Маппер для одной точки данных (остается почти без изменений)
fun SleepDataPieceDto.toDomain(): SleepDataPiece? {
    val instant = try {
        this.timestamp?.let { Instant.parse(it) }
    } catch (e: DateTimeParseException) {
        println("Error parsing timestamp: ${this.timestamp}, error: ${e.message}")
        null
    }

    val phase = try {
        this.phase?.let { SleepPhase.valueOf(it.uppercase()) } ?: SleepPhase.UNKNOWN
    } catch (e: IllegalArgumentException) {
        println("Error parsing phase: ${this.phase}, error: ${e.message}")
        SleepPhase.UNKNOWN
    }

    return if (instant != null && this.pulse != null) {
        SleepDataPiece(
            timestamp = instant,
            pulse = this.pulse,
            phase = phase
        )
    } else {
        null
    }
}

// Функция для расчета качества сна (ЗАГЛУШКА - нужна реальная логика!)
private fun calculateSleepQuality(totalSleep: Duration?, awakenings: Int?): Int {
    if (totalSleep == null) return 0 // Не можем рассчитать без длительности сна

    val hoursSlept = totalSleep.toHours().toInt()
    val awakeningsCount = awakenings ?: 0

    // Очень примитивная формула: больше сна - лучше, меньше пробуждений - лучше
    var score = 0
    score += when {
        hoursSlept >= 9 -> 50
        hoursSlept >= 7 -> 40
        hoursSlept >= 5 -> 25
        else -> 10
    }

    score += when {
        awakeningsCount == 0 -> 40
        awakeningsCount <= 2 -> 30
        awakeningsCount <= 5 -> 15
        else -> 5
    }

    // Нормализация до 0-100 (примерно)
    return (score * 1.1).roundToInt().coerceIn(0, 100)
}

// Функция для расчета распределения сна по дням недели
private fun calculateWeekdayDistribution(sleepData: SleepData?): WeekdaySleepDistribution? {
    if (sleepData.isNullOrEmpty()) return null

    // Группируем данные по дням недели
    val sleepByDay = sleepData
        .groupBy { it.timestamp.atZone(ZoneId.systemDefault()).dayOfWeek } // Группируем по java.time.DayOfWeek
        .mapValues { (_, pieces) -> calculateTotalSleepDurationForPieces(pieces) } // Считаем длительность для каждой группы

    // Конвертируем в вашу модель WeekdaySleepDistribution
    val distribution = DayOfWeek.values().map { javaDayOfWeek ->
        val domainWeekday = mapJavaDayToDomainWeekday(javaDayOfWeek) // Маппер дней недели
        val duration = sleepByDay[javaDayOfWeek] ?: Duration.ZERO // Берем посчитанную длительность или 0
        val hours = duration.toMillis() / (1000.0 * 60 * 60) // Конвертируем в double часы
        WeekdaySleep(weekday = domainWeekday, asleepHours = hours)
    }

    return distribution
}

// Вспомогательная функция для подсчета длительности сна в списке SleepDataPiece
private fun calculateTotalSleepDurationForPieces(pieces: List<SleepDataPiece>): Duration {
    var totalDuration = Duration.ZERO
    // Сортируем на всякий случай
    val sortedPieces = pieces.sortedBy { it.timestamp }

    for (i in 0 until sortedPieces.size - 1) {
        // Учитываем только те промежутки, когда фаза была не AWAKE
        if (sortedPieces[i].phase != SleepPhase.AWAKE) {
            val durationBetweenPoints = Duration.between(sortedPieces[i].timestamp, sortedPieces[i + 1].timestamp)
            // Добавляем длительность, но не больше разумного предела (например, 30 минут),
            // чтобы избежать больших скачков при редких данных
            if (durationBetweenPoints.abs().toMinutes() < 30) {
                totalDuration = totalDuration.plus(durationBetweenPoints)
            }
        }
    }
    return totalDuration
}

// Вспомогательный маппер для дней недели (java.time -> ваш enum)
private fun mapJavaDayToDomainWeekday(javaDayOfWeek: DayOfWeek): Weekday {
    return when (javaDayOfWeek) {
        DayOfWeek.MONDAY -> Weekday.Mon
        DayOfWeek.TUESDAY -> Weekday.Tue
        DayOfWeek.WEDNESDAY -> Weekday.Wed
        DayOfWeek.THURSDAY -> Weekday.Thu
        DayOfWeek.FRIDAY -> Weekday.Fri
        DayOfWeek.SATURDAY -> Weekday.Sat
        DayOfWeek.SUNDAY -> Weekday.Sun
    }
}