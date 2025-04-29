//[entry_microservice](../../index.md)/[ru.itmo.keydb](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [KeyDBAPI](-key-d-b-a-p-i/index.md) | [jvm]<br>object [KeyDBAPI](-key-d-b-a-p-i/index.md)<br>Объект API для отправки запросов другим микросервисам через KeyDB |
| [Logger](-logger/index.md) | [jvm]<br>object [Logger](-logger/index.md) |
| [LoggerRequest](-logger-request/index.md) | [jvm]<br>data class [LoggerRequest](-logger-request/index.md)(val name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val level: [LogLevel](-log-level/index.md), val stacktrace: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null) |
| [LogLevel](-log-level/index.md) | [jvm]<br>enum [LogLevel](-log-level/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[LogLevel](-log-level/index.md)&gt; |