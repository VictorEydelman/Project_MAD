# Sleep Monitoring API – Документация

Бэкенд-приложение на Ktor для сбора и анализа данных сна пользователя.

Базовый URL: `/api/v1`

## ⚙️ Особенности сериализации

Для корректной (де-)сериализации объектов необходимо использовать
Jackson object mapper с модулем JavaTimeModule (jackson-datatype-jsr310), в KeyDBClient уже настроен:

```kotlin
val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
```

---

## 🧱 1. Модель данных

### Enum'ы

```kotlin
enum class Gender {
    Male, Female, Null
}

enum class Periodicity {
    OnceADay, TwiceADay, ThreeTimesADay,
    OnceEveryTwoDays, TwiceAWeek, ThreeTimesAWeek,
    Rarely, Often, Never, Null
}

enum class SleepPhase {
    AWAKE, DROWSY, LIGHT, DEEP, REM
}

enum class Weekday {
    Mon, Tue, Wed, Thu, Fri, Sat, Sun
}
```

### Основные data классы

```kotlin
data class Alarm(
    val time: LocalTime,
    val alarm: Boolean,
)

data class BedTime(
    val time: LocalTime,
    val remindMeToSleep: Boolean,
    val remindBeforeBad: Boolean,
)

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

data class SleepDataPiece(
    val timestamp: Instant,
    val pulse: Int,
    val phase: SleepPhase,
)

typealias SleepData = List<SleepDataPiece>

data class WeekdaySleep(
    val weekday: Weekday,
    val asleepHours: Double,
)

typealias WeekdaySleepDistribution = List<WeekdaySleep>

data class TimePreference(
    val asleepTime: LocalTime,
    val awakeTime: LocalTime,
)

data class User(
    val username: String,
    val password: String,
)
```

### DTO API

```kotlin
data class AuthRequest(
    val username: String,
    val password: String,
)

data class AuthResponse(
    val username: String,
    val token: String,
    val success: Boolean = true,
)

data class CheckAuthResponse(
    val username: String?,
    val success: Boolean,
)

data class DataResponse<T>(
    val data: T,
    val message: String? = null,
    val success: Boolean = true,
)

data class SimpleResponse(
    val success: Boolean,
    val message: String?,
)
```

### DTO KeyDB

```kotlin
data class UserRequest<T>(
    val username: String,
    val data: T,
)
```

---

## 🌐 2. API Маршруты (Routes)

### 🔐 `/auth`

| Метод  | Путь           | Тело запроса       | Ответ            |
|--------|----------------|--------------------|------------------|
| POST   | /register      | `AuthRequest`      | `AuthResponse`   |
| POST   | /login         | `AuthRequest`      | `AuthResponse`   |
| GET    | /check         | (авторизация JWT)  | `CheckAuthResponse` |

---

### 👤 `/profile`

| Метод  | Путь                   | Тело запроса | Ответ              |
|--------|------------------------|--------------|--------------------|
| POST   | /update                | `Profile`    | `SimpleResponse`   |
| GET    | /get                   | -            | `DataResponse<Profile>` |
| POST   | /clear-temporaries     | -            | `SimpleResponse`   |

---

### 💤 `/sleep`

| Метод  | Путь                          | Тело запроса         | Ответ                        |
|--------|-------------------------------|----------------------|------------------------------|
| POST   | /upload                       | `SleepData`          | `SimpleResponse`             |
| GET    | /daily-report                 | -                    | `DataResponse<Report>`       |
| GET    | /weekly-report                | -                    | `DataResponse<Report>`       |
| GET    | /all-time-report              | -                    | `DataResponse<Report>`       |
| GET    | /recommended-times            | -                    | `DataResponse<TimePreference>` |

---

## 🔌 3. KeyDB API

### 📦 Каналы (Pub/Sub) через KeyDB

#### 🔐 Авторизация

| Канал       | Тип запроса     | Ответ          | Микросервис |
|-------------|------------------|----------------|--------------|
| `get-user`  | `String` (username) | `User?`       | DB          |
| `save-user` | `User`             | `Boolean`     | DB          |

---

#### 👤 Профиль

| Канал                  | Тип запроса                        | Ответ      | Микросервис |
|------------------------|-------------------------------------|------------|-------------|
| `update-profile`       | `UserRequest<Profile>`             | `Boolean`  | DB          |
| `get-profile`          | `String` (username)                | `Profile`  | DB          |
| `clear-profile-temporaries` | `String` (username)          | `Boolean`  | DB          |

---

#### 💤 Сон

| Канал                 | Тип запроса                                | Ответ        | Микросервис |
|-----------------------|---------------------------------------------|--------------|-------------|
| `upload-sleep`        | `UserRequest<SleepData>`                   | `Boolean`    | DB          |
| `make-daily-report`   | `String` (username)                        | `Report`     | Logic       |
| `make-weekly-report`  | `String` (username)                        | `Report`     | Logic       |
| `make-all-time-report`| `String` (username)                        | `Report`     | Logic       |
| `calculate-recommended-times` | `String` (username)               | `TimePreference` | Logic   |

---
