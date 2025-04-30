//[entry_microservice](../../../index.md)/[ru.itmo.model](../index.md)/[Report](index.md)

# Report

data class [Report](index.md)(val quality: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), val startTime: [LocalTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html), val endTime: [LocalTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html), val totalSleep: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html), val awakenings: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), val avgAwake: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html), val avgAsleep: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html), val avgToFallAsleep: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html), val data: [SleepData](../-sleep-data/index.md)?, val distribution: [WeekdaySleepDistribution](../-weekday-sleep-distribution/index.md)?)

Объект отчёта за период для построения статистики

#### Parameters

jvm

| | |
|---|---|
| quality | Качество сна (от 0 до 100) |
| startTime | Время, когда пользователь уснул первый раз за период |
| endTime | Время, когда пользователь проснулся последний раз за период |
| totalSleep | Суммарное время нахождения во сне |
| awakenings | Среднее количество пробуждений во время сессии сна (без учета последнего окончательного пробуждения) |
| avgAwake | Среднее время бодрствований внутри сессии сна (от первого засыпания до окончательного пробуждения) |
| avgAsleep | Среднее время сессий сна |
| avgToFallAsleep | Среднее время, понадобившееся пользователю чтобы заснуть |
| data | Подробные данные сна за период. Нужны только для дневного (daily) отчёта |
| distribution | Распределение времени сна по дням за неделю. Нужно только для недельного (weekly) отчёта |

## Constructors

| | |
|---|---|
| [Report](-report.md) | [jvm]<br>constructor(quality: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), startTime: [LocalTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html), endTime: [LocalTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html), totalSleep: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html), awakenings: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), avgAwake: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html), avgAsleep: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html), avgToFallAsleep: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html), data: [SleepData](../-sleep-data/index.md)?, distribution: [WeekdaySleepDistribution](../-weekday-sleep-distribution/index.md)?) |

## Properties

| Name | Summary |
|---|---|
| [avgAsleep](avg-asleep.md) | [jvm]<br>val [avgAsleep](avg-asleep.md): [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html) |
| [avgAwake](avg-awake.md) | [jvm]<br>val [avgAwake](avg-awake.md): [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html) |
| [avgToFallAsleep](avg-to-fall-asleep.md) | [jvm]<br>val [avgToFallAsleep](avg-to-fall-asleep.md): [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html) |
| [awakenings](awakenings.md) | [jvm]<br>val [awakenings](awakenings.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [data](data.md) | [jvm]<br>val [data](data.md): [SleepData](../-sleep-data/index.md)? |
| [distribution](distribution.md) | [jvm]<br>val [distribution](distribution.md): [WeekdaySleepDistribution](../-weekday-sleep-distribution/index.md)? |
| [endTime](end-time.md) | [jvm]<br>val [endTime](end-time.md): [LocalTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html) |
| [quality](quality.md) | [jvm]<br>val [quality](quality.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [startTime](start-time.md) | [jvm]<br>val [startTime](start-time.md): [LocalTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html) |
| [totalSleep](total-sleep.md) | [jvm]<br>val [totalSleep](total-sleep.md): [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html) |