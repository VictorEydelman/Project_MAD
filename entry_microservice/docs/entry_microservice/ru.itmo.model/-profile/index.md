//[entry_microservice](../../../index.md)/[ru.itmo.model](../index.md)/[Profile](index.md)

# Profile

data class [Profile](index.md)(val name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val surname: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val birthday: [LocalDate](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html), val gender: [Gender](../-gender/index.md), val physicalCondition: [Periodicity](../-periodicity/index.md), val caffeineUsage: [Periodicity](../-periodicity/index.md), val alcoholUsage: [Periodicity](../-periodicity/index.md), val alarmRecurring: [Alarm](../-alarm/index.md)?, var alarmTemporary: [Alarm](../-alarm/index.md)?, val bedTimeRecurring: [BedTime](../-bed-time/index.md)?, var bedTimeTemporary: [BedTime](../-bed-time/index.md)?)

Объект профиля (настроек) пользователя

#### Parameters

jvm

| | |
|---|---|
| name | Имя |
| surname | Фамилия |
| birthday | Дата рождения |
| gender | Пол |
| physicalCondition | Периодичность занятий спортом |
| caffeineUsage | Переодичность потребления кофеина |
| alcoholUsage | Переодичность потребления алкоголя |
| alarmRecurring | Постоянный повторяющийся будильник |
| alarmTemporary | Временный сбрасывающийся после звонка будильник |
| bedTimeRecurring | Заданное постоянное желаемое время отхода ко сну |
| bedTimeTemporary | Временное сбрасывающееся после уведомления отхода ко сну |

## Constructors

| | |
|---|---|
| [Profile](-profile.md) | [jvm]<br>constructor(name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), surname: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), birthday: [LocalDate](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html), gender: [Gender](../-gender/index.md), physicalCondition: [Periodicity](../-periodicity/index.md), caffeineUsage: [Periodicity](../-periodicity/index.md), alcoholUsage: [Periodicity](../-periodicity/index.md), alarmRecurring: [Alarm](../-alarm/index.md)?, alarmTemporary: [Alarm](../-alarm/index.md)?, bedTimeRecurring: [BedTime](../-bed-time/index.md)?, bedTimeTemporary: [BedTime](../-bed-time/index.md)?) |

## Properties

| Name | Summary |
|---|---|
| [alarmRecurring](alarm-recurring.md) | [jvm]<br>val [alarmRecurring](alarm-recurring.md): [Alarm](../-alarm/index.md)? |
| [alarmTemporary](alarm-temporary.md) | [jvm]<br>var [alarmTemporary](alarm-temporary.md): [Alarm](../-alarm/index.md)? |
| [alcoholUsage](alcohol-usage.md) | [jvm]<br>val [alcoholUsage](alcohol-usage.md): [Periodicity](../-periodicity/index.md) |
| [bedTimeRecurring](bed-time-recurring.md) | [jvm]<br>val [bedTimeRecurring](bed-time-recurring.md): [BedTime](../-bed-time/index.md)? |
| [bedTimeTemporary](bed-time-temporary.md) | [jvm]<br>var [bedTimeTemporary](bed-time-temporary.md): [BedTime](../-bed-time/index.md)? |
| [birthday](birthday.md) | [jvm]<br>val [birthday](birthday.md): [LocalDate](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html) |
| [caffeineUsage](caffeine-usage.md) | [jvm]<br>val [caffeineUsage](caffeine-usage.md): [Periodicity](../-periodicity/index.md) |
| [gender](gender.md) | [jvm]<br>val [gender](gender.md): [Gender](../-gender/index.md) |
| [name](name.md) | [jvm]<br>val [name](name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [physicalCondition](physical-condition.md) | [jvm]<br>val [physicalCondition](physical-condition.md): [Periodicity](../-periodicity/index.md) |
| [surname](surname.md) | [jvm]<br>val [surname](surname.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |