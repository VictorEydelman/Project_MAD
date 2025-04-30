//[entry_microservice](../../../index.md)/[[root]](../index.md)/[KeyDBClient](index.md)/[subscribeWithResponse](subscribe-with-response.md)

# subscribeWithResponse

[jvm]\
suspend fun [subscribeWithResponse](subscribe-with-response.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), messageHandler: ([String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), responseTTL: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html) = 10)

Подписывается на указанный канал для приёма запросов и отправки ответов.

[jvm]\
suspend fun &lt;[TRequest](subscribe-with-response.md) : [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html), [TResponse](subscribe-with-response.md) : [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt; [subscribeWithResponse](subscribe-with-response.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), requestType: [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;[TRequest](subscribe-with-response.md)&gt;, messageHandler: ([TRequest](subscribe-with-response.md)) -&gt; [TResponse](subscribe-with-response.md)?, responseTTL: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html) = 10)

suspend fun &lt;[TRequest](subscribe-with-response.md) : [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html), [TResponse](subscribe-with-response.md) : [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt; [subscribeWithResponse](subscribe-with-response.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), requestType: TypeReference&lt;[TRequest](subscribe-with-response.md)&gt;, messageHandler: ([TRequest](subscribe-with-response.md)) -&gt; [TResponse](subscribe-with-response.md)?, responseTTL: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html) = 10)