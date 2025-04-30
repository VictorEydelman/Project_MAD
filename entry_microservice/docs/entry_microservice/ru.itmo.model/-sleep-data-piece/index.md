//[entry_microservice](../../../index.md)/[ru.itmo.model](../index.md)/[SleepDataPiece](index.md)

# SleepDataPiece

data class [SleepDataPiece](index.md)(val timestamp: [LocalDateTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html), val pulse: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), val sleepPhase: [SleepPhase](../-sleep-phase/index.md))

Часть данных о сне пользователя

#### Parameters

jvm

| | |
|---|---|
| timestamp | Временная отметка |
| pulse | Пульс пользователя в данное время |
| sleepPhase | Фаза сна в данное время |

## Constructors

| | |
|---|---|
| [SleepDataPiece](-sleep-data-piece.md) | [jvm]<br>constructor(timestamp: [LocalDateTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html), pulse: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), sleepPhase: [SleepPhase](../-sleep-phase/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [pulse](pulse.md) | [jvm]<br>val [pulse](pulse.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [sleepPhase](sleep-phase.md) | [jvm]<br>val [sleepPhase](sleep-phase.md): [SleepPhase](../-sleep-phase/index.md) |
| [timestamp](timestamp.md) | [jvm]<br>val [timestamp](timestamp.md): [LocalDateTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html) |