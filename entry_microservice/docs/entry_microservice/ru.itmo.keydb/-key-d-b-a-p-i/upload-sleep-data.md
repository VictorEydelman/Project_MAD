//[entry_microservice](../../../index.md)/[ru.itmo.keydb](../index.md)/[KeyDBAPI](index.md)/[uploadSleepData](upload-sleep-data.md)

# uploadSleepData

[jvm]\
suspend fun [uploadSleepData](upload-sleep-data.md)(username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), sleepData: [SleepData](../../ru.itmo.model/-sleep-data/index.md))

Загрузить массив данных [SleepData](../../ru.itmo.model/-sleep-data/index.md) сна пользователя в DB DB должен прослушивать канал &quot;upload-sleep&quot;