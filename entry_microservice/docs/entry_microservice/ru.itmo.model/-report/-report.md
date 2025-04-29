//[entry_microservice](../../../index.md)/[ru.itmo.model](../index.md)/[Report](index.md)/[Report](-report.md)

# Report

[jvm]\
constructor(quality: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), startTime: [LocalTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html), endTime: [LocalTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html), totalSleep: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html), awakenings: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), avgAwake: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html), avgAsleep: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html), avgToFallAsleep: [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html), data: [SleepData](../-sleep-data/index.md)?, distribution: [WeekdaySleepDistribution](../-weekday-sleep-distribution/index.md)?)

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