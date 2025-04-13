# DB_server

Микросервис для подключения к бд postgres и clickhouse.

## Для внешних пользователей предоставляет:

### Таблица Users:

USERS (username VARCHAR(255) PRIMARY KEY, password VARCHAR(255))

#### Типы данных:

Users(val username: String, val password: String)

#### Запросы:

save-user - запрос который принимает Users и возвращает Boolean
get-user - запрос который принимает username: String и возвращает Users
user-not-exist - запрос который принимает username: String и возвращает Boolean, если пользователя нету то true, иначе false

### Таблица Settings:

SETTINGS (USERNAME VARCHAR(255) PRIMARY KEY, BIRTHDAY DATE, GENDER GENDER, PHYSICALCONDITION Frequency, CaffeineUsage Frequency, AlcoholUsage Frequency)

#### Типы данных:

Settings(val id: Int, val username: String, @Contextual val birthday: Date, val gender: Gender, val physicalCondition: Frequency, val caffeineUsage: Frequency, val alcoholUsage: Frequency)

Frequency {OnceADay, TwiceADay, ThreeTimesADay, OnceEveryTwoDays, TwiceAWeek, ThreeTimesAWeek, Rarely, Often, Never, Null}

Gender {Men, Woman, Null}

#### Запросы:

save-setting - запрос который принимает Settings и возвращает boolean
update-setting - запрос который принимает Settings и возвращает boolean
get-setting - запрос который принимает username: String и возвращает Settings

### Таблица SleepStatistic

SleepStatistic (username String,
                timestamp DateTime,
                pulse UFloat32,
                sleep_phase Enum8('drowsiness' = 1, 'shallow' = 2, 'deep' = 3, 'fast ' = 4, 'wakefulness' = 5))

#### Типы данных:

SleepStatistic(username: String, timestamp: Timestamp, pulse: Double, sleepPhase: Int)

#### Запросы:

save-sleepStatistic - запрос который принимает SleepStatistic и возвращает Boolean
get-SleepStatistic-Interval - запрос который (username: String, start: Timestamp, end: Timestamp) и возращает List<Map<String, Any>> каждая Map состоит из (username: String,  timestamp: Timestamp, pulse: Double, sleepPhase: Int)
