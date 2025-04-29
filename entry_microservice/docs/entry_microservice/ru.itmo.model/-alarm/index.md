//[entry_microservice](../../../index.md)/[ru.itmo.model](../index.md)/[Alarm](index.md)

# Alarm

data class [Alarm](index.md)(val time: [LocalTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html), val alarm: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html))

Объект будильника для сохранения

#### Parameters

jvm

| | |
|---|---|
| time | Время будильника |
| alarm | Активен ли будильник |

## Constructors

| | |
|---|---|
| [Alarm](-alarm.md) | [jvm]<br>constructor(time: [LocalTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html), alarm: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [alarm](alarm.md) | [jvm]<br>val [alarm](alarm.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [time](time.md) | [jvm]<br>val [time](time.md): [LocalTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html) |