//[entry_microservice](../../index.md)/[ru.itmo.dto.api](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [AuthRequest](-auth-request/index.md) | [jvm]<br>data class [AuthRequest](-auth-request/index.md)(val username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val password: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) |
| [AuthResponse](-auth-response/index.md) | [jvm]<br>data class [AuthResponse](-auth-response/index.md)(val username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val token: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val success: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = true) |
| [CheckAuthResponse](-check-auth-response/index.md) | [jvm]<br>data class [CheckAuthResponse](-check-auth-response/index.md)(val username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, val success: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)) |
| [DataResponse](-data-response/index.md) | [jvm]<br>data class [DataResponse](-data-response/index.md)&lt;[T](-data-response/index.md)&gt;(val data: [T](-data-response/index.md), val message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, val success: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = true) |
| [SimpleResponse](-simple-response/index.md) | [jvm]<br>data class [SimpleResponse](-simple-response/index.md)(val success: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), val message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?) |