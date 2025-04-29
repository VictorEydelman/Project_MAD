# Logic Microservice

## Микросервис с реализацией основной бизнес-логики обработки данных

Этот микросервис предназначен для обработки данных, передачи их на фронтенд часть.

---

### Запросы

- **make-daily-report**:
    - **Описание**: Формирует дневной отчет о сне
    - **Параметры**: String (username)
    - **Ответ**: Report

- **make-weekly-report**:
    - **Описание**: Формирует недельный отчет о сне
    - **Параметры**: String (username)
    - **Ответ**: Report

- **make-all-time-report**:
    - **Описание**: Формирует отчет о сне за все время использования
    - **Параметры**: String (username)
    - **Ответ**: Report

- **calculate-recommended-times**:
    - **Описание**: Рассчитывает рекомендуемое количество сна
    - **Параметры**: String (username)
    - **Ответ**: TimePreference


### Тип данных

```kotlin
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
enum class SleepPhase {
    AWAKE, DROWSY, LIGHT, DEEP, REM
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class SleepDataPiece(
    val timestamp: LocalDateTime,
    val pulse: Int,
    val sleepPhase: SleepPhase,
)

typealias SleepData = List<SleepDataPiece>

enum class Weekday {
    Mon, Tue, Wed, Thu, Fri, Sat, Sun
}

data class WeekdaySleep(
    val weekday: Weekday,
    val asleepHours: Double,
)

typealias WeekdaySleepDistribution = List<WeekdaySleep>
data class TimePreference(
    val asleepTime: LocalTime,
    val awakeTime: LocalTime,
)
```
