//[entry_microservice](../../../index.md)/[ru.itmo.keydb](../index.md)/[KeyDBAPI](index.md)

# KeyDBAPI

[jvm]\
object [KeyDBAPI](index.md)

Объект API для отправки запросов другим микросервисам через KeyDB

## Functions

| Name | Summary |
|---|---|
| [calculateRecommendedTimes](calculate-recommended-times.md) | [jvm]<br>suspend fun [calculateRecommendedTimes](calculate-recommended-times.md)(username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [TimePreference](../../ru.itmo.model/-time-preference/index.md)<br>Рассчитать рекомендумое время отхода ко сну и просыпания [TimePreference](../../ru.itmo.model/-time-preference/index.md) через Logic Logic должен прослушивать канал &quot;calculate-recommended-times&quot; |
| [clearProfileTemporaries](clear-profile-temporaries.md) | [jvm]<br>suspend fun [clearProfileTemporaries](clear-profile-temporaries.md)(username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html))<br>Очистить временные настройки пользователя в DB. Очищает временный будильник [Profile.alarmTemporary](../../ru.itmo.model/-profile/alarm-temporary.md) и время отхода ко сну [Profile.bedTimeTemporary](../../ru.itmo.model/-profile/bed-time-temporary.md) DB должен прослушивать канал &quot;clear-profile-temporaries&quot; |
| [close](close.md) | [jvm]<br>fun [close](close.md)() |
| [getProfile](get-profile.md) | [jvm]<br>suspend fun [getProfile](get-profile.md)(username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [Profile](../../ru.itmo.model/-profile/index.md)<br>Получить профиль [Profile](../../ru.itmo.model/-profile/index.md) пользователя из DB DB должен прослушивать канал &quot;get-profile&quot; |
| [getUser](get-user.md) | [jvm]<br>suspend fun [getUser](get-user.md)(username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [User](../../ru.itmo.model/-user/index.md)?<br>Получить пользователя по никнейму у микросервиса DB DB должен прослушивать канал &quot;get-user&quot; |
| [init](init.md) | [jvm]<br>fun [init](init.md)(host: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), port: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)) |
| [makeSleepReport](make-sleep-report.md) | [jvm]<br>suspend fun [makeSleepReport](make-sleep-report.md)(username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), period: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [Report](../../ru.itmo.model/-report/index.md)<br>Создать отчет [Report](../../ru.itmo.model/-report/index.md) по сну через Logic за один из трех периодов Logic должен прослушивать каналы: &quot;make-daily-report&quot;, &quot;make-weekly-report&quot;, &quot;make-all-time-report&quot; |
| [saveUser](save-user.md) | [jvm]<br>suspend fun [saveUser](save-user.md)(user: [User](../../ru.itmo.model/-user/index.md))<br>Сохранить пользователя в микросервис DB DB должен прослушивать канал &quot;save-user&quot; |
| [updateProfile](update-profile.md) | [jvm]<br>suspend fun [updateProfile](update-profile.md)(username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), profile: [Profile](../../ru.itmo.model/-profile/index.md))<br>Обновить профиль [Profile](../../ru.itmo.model/-profile/index.md) пользователя в DB DB должен прослушивать канал &quot;update-profile&quot; |
| [uploadSleepData](upload-sleep-data.md) | [jvm]<br>suspend fun [uploadSleepData](upload-sleep-data.md)(username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), sleepData: [SleepData](../../ru.itmo.model/-sleep-data/index.md))<br>Загрузить массив данных [SleepData](../../ru.itmo.model/-sleep-data/index.md) сна пользователя в DB DB должен прослушивать канал &quot;upload-sleep&quot; |