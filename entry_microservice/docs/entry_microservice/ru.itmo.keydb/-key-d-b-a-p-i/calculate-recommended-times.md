//[entry_microservice](../../../index.md)/[ru.itmo.keydb](../index.md)/[KeyDBAPI](index.md)/[calculateRecommendedTimes](calculate-recommended-times.md)

# calculateRecommendedTimes

[jvm]\
suspend fun [calculateRecommendedTimes](calculate-recommended-times.md)(username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [TimePreference](../../ru.itmo.model/-time-preference/index.md)

Рассчитать рекомендумое время отхода ко сну и просыпания [TimePreference](../../ru.itmo.model/-time-preference/index.md) через Logic Logic должен прослушивать канал &quot;calculate-recommended-times&quot;