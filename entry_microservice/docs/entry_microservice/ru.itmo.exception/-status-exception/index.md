//[entry_microservice](../../../index.md)/[ru.itmo.exception](../index.md)/[StatusException](index.md)

# StatusException

[jvm]\
class [StatusException](index.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val statusCode: HttpStatusCode = HttpStatusCode.BadRequest) : [RuntimeException](https://docs.oracle.com/javase/8/docs/api/java/lang/RuntimeException.html)

## Constructors

| | |
|---|---|
| [StatusException](-status-exception.md) | [jvm]<br>constructor(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), statusCode: HttpStatusCode = HttpStatusCode.BadRequest) |

## Properties

| Name | Summary |
|---|---|
| [cause](index.md#-654012527%2FProperties%2F-1216412040) | [jvm]<br>open val [cause](index.md#-654012527%2FProperties%2F-1216412040): [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)? |
| [message](index.md#1824300659%2FProperties%2F-1216412040) | [jvm]<br>open val [message](index.md#1824300659%2FProperties%2F-1216412040): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [statusCode](status-code.md) | [jvm]<br>val [statusCode](status-code.md): HttpStatusCode |