//[entry_microservice](../../../index.md)/[ru.itmo.model](../index.md)/[BedTime](index.md)

# BedTime

data class [BedTime](index.md)(val time: [LocalTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html), val remindMeToSleep: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), val remindBeforeBad: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html))

Объект рекомендуемого отхода ко сну для сохранения

#### Parameters

jvm

| | |
|---|---|
| time | Время отхода ко сну |
| remindMeToSleep | Нужно ли уведомление в это время |
| remindBeforeBad | Нужно ли уведомление за 15 минут до этого времени |

## Constructors

| | |
|---|---|
| [BedTime](-bed-time.md) | [jvm]<br>constructor(time: [LocalTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html), remindMeToSleep: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), remindBeforeBad: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [remindBeforeBad](remind-before-bad.md) | [jvm]<br>val [remindBeforeBad](remind-before-bad.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [remindMeToSleep](remind-me-to-sleep.md) | [jvm]<br>val [remindMeToSleep](remind-me-to-sleep.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [time](time.md) | [jvm]<br>val [time](time.md): [LocalTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html) |