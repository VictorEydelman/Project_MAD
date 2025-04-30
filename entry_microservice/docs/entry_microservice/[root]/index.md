//[entry_microservice](../../index.md)/[[root]](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [KeyDBClient](-key-d-b-client/index.md) | [jvm]<br>class [KeyDBClient](-key-d-b-client/index.md)(host: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = &quot;keydb&quot;, port: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 6379, poolConfig: JedisPoolConfig = defaultPoolConfig())<br>Класс KeyDBClient реализует коммуникацию через KeyDB/Redis для обмена запросами и ответами. |
| [UIDMessage](-u-i-d-message/index.md) | [jvm]<br>data class [UIDMessage](-u-i-d-message/index.md)(val uid: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) |