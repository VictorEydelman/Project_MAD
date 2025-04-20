# DB_server

## Микросервис для работы с базами данных PostgreSQL и ClickHouse

Этот микросервис предоставляет API для взаимодействия с базами данных PostgreSQL и ClickHouse. Он предназначен для управления пользователями, настройками и статистикой сна.

### Структура микросервиса

Микросервис включает в себя три основные таблицы: Users, Settings и SleepStatistic.

---

## Таблица Users

### Структура таблицы

```sql
USERS (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255)
)
```


### Тип данных
```kotlin
data class Users(val username: String, val password: String)
```

### Запросы

- **save-user**: 
  - **Описание**: Сохраняет пользователя.
  - **Параметры**: Users
  - **Ответ**: Boolean

- **get-user**: 
  - **Описание**: Получает пользователя по имени.
  - **Параметры**: username: String
  - **Ответ**: Users

---

## Таблица Settings

### Структура таблицы

```sql
SETTINGS (
     USERNAME VARCHAR(255) PRIMARY KEY REFERENCES users (username),
     NAME VARCHAR(255),
     SURNAME VARCHAR(255),
     BIRTHDAY DATE,
     GENDER gender,
     PHYSICALCONDITION frequency,
     CAFFEINEUSAGE frequency,
     ALCOHOLUSAGE frequency,
     alarmRecurring integer NULL,
     alarmTemporary integer NULL,
     bedTimeRecurring integer NULL,
     bedTimeTemporary integer NULL        
)
```

### Тип данных

```kotlin
data class Settings(
     val username: String,
     val name: String,
     val surname: String,
     @Contextual val birthday: Date,
     var gender: Gender,
     val physicalCondition: Frequency,
     val caffeineUsage: Frequency,
     val alcoholUsage: Frequency,
     val alarmRecurring: Alarm?,
     var alarmTemporary: Alarm?,
     val bedTimeRecurring: BedTime?,
     var bedTimeTemporary: BedTime?)
```

### Перечисления

```kotlin
enum class Frequency {
    OnceADay, TwiceADay, ThreeTimesADay, OnceEveryTwoDays, TwiceAWeek, ThreeTimesAWeek, Rarely, Often, Never, Null
}

enum class Gender {
    male, female, Null
}
```

### Запросы

- **save-setting**: 
  - **Описание**: Сохраняет или обновляет настройки пользователя.
  - **Параметры**: Settings
  - **Ответ**: Boolean

- **get-setting**: 
  - **Описание**: Получает настройки пользователя по имени.
  - **Параметры**: username: String
  - **Ответ**: Settings

---

## Таблица SleepStatistic

### Структура таблицы

```sql
SleepStatistic (
     username String,
     timestamp DateTime,
     pulse Float32,
     sleep_phase Enum8('AWAKE' = 1, 'DROWSY' = 2, 'LIGHT' = 3, 'DEEP' = 4, 'REM' = 5)
)
```

### Тип данных

```kotlin
data class SleepStatistic(
  val username: String, 
  @Contextual val timestamp: LocalDateTime, 
  val pulse: Float, 
  val sleepPhase: String
)

data class SleepInterval(
  val username: String, 
  @Contextual val start: LocalDateTime,
  @Contextual val end: LocalDateTime
)

```

### Запросы

- **save-sleepStatistic**:
  - **Описание**: Сохраняет статистику сна.
  - **Параметры**: SleepStatistic
  - **Ответ**: Boolean

- **get-sleepStatistic-Interval**:
  - **Описание**: Получает статистику сна за указанный интервал.
  - **Параметры**: SleepInterval
  - **Ответ**: List<SleepStatistic>

---



