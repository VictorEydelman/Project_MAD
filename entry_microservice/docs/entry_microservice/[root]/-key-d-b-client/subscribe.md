//[entry_microservice](../../../index.md)/[[root]](../index.md)/[KeyDBClient](index.md)/[subscribe](subscribe.md)

# subscribe

[jvm]\
suspend fun [subscribe](subscribe.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), onMessage: ([String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html), repeat: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false)

suspend fun &lt;[TMessage](subscribe.md) : [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt; [subscribe](subscribe.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), onMessage: ([TMessage](subscribe.md)) -&gt; [Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html), messageType: [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;[TMessage](subscribe.md)&gt;, repeat: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false)