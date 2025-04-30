//[entry_microservice](../../../index.md)/[ru.itmo.keydb](../index.md)/[LoggerRequest](index.md)

# LoggerRequest

[jvm]\
data class [LoggerRequest](index.md)(val name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val level: [LogLevel](../-log-level/index.md), val stacktrace: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null)

## Constructors

| | |
|---|---|
| [LoggerRequest](-logger-request.md) | [jvm]<br>constructor(name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), level: [LogLevel](../-log-level/index.md), stacktrace: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [level](level.md) | [jvm]<br>val [level](level.md): [LogLevel](../-log-level/index.md) |
| [message](message.md) | [jvm]<br>val [message](message.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [name](name.md) | [jvm]<br>val [name](name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [stacktrace](stacktrace.md) | [jvm]<br>val [stacktrace](stacktrace.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null |