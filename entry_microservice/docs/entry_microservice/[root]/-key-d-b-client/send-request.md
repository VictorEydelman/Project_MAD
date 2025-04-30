//[entry_microservice](../../../index.md)/[[root]](../index.md)/[KeyDBClient](index.md)/[sendRequest](send-request.md)

# sendRequest

[jvm]\
suspend fun &lt;[TRequest](send-request.md) : [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html), [TResponse](send-request.md) : [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt; [sendRequest](send-request.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), message: [TRequest](send-request.md), responseType: [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;[TResponse](send-request.md)&gt;, timeoutMs: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html) = 5000): [TResponse](send-request.md)?

Отправляет запрос через указанный канал и ожидает ответ.

[jvm]\
suspend fun [sendRequest](send-request.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), timeoutMs: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html) = 5000): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?

Отправляет запрос как JSON-строку, ожидает ответ по ключу в Redis.