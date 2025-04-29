//[entry_microservice](../../../index.md)/[ru.itmo.keydb](../index.md)/[KeyDBAPI](index.md)/[makeSleepReport](make-sleep-report.md)

# makeSleepReport

[jvm]\
suspend fun [makeSleepReport](make-sleep-report.md)(username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), period: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [Report](../../ru.itmo.model/-report/index.md)

Создать отчет [Report](../../ru.itmo.model/-report/index.md) по сну через Logic за один из трех периодов Logic должен прослушивать каналы: &quot;make-daily-report&quot;, &quot;make-weekly-report&quot;, &quot;make-all-time-report&quot;

#### Parameters

jvm

| | |
|---|---|
| period | Пероид сна: &quot;daily&quot; | &quot;weekly&quot; | &quot;all-time&quot; |