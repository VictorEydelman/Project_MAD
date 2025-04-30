//[entry_microservice](../../../index.md)/[ru.itmo.keydb](../index.md)/[KeyDBAPI](index.md)/[clearProfileTemporaries](clear-profile-temporaries.md)

# clearProfileTemporaries

[jvm]\
suspend fun [clearProfileTemporaries](clear-profile-temporaries.md)(username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html))

Очистить временные настройки пользователя в DB. Очищает временный будильник [Profile.alarmTemporary](../../ru.itmo.model/-profile/alarm-temporary.md) и время отхода ко сну [Profile.bedTimeTemporary](../../ru.itmo.model/-profile/bed-time-temporary.md) DB должен прослушивать канал &quot;clear-profile-temporaries&quot;