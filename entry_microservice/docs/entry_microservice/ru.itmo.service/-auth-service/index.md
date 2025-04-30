//[entry_microservice](../../../index.md)/[ru.itmo.service](../index.md)/[AuthService](index.md)

# AuthService

[jvm]\
object [AuthService](index.md)

## Functions

| Name | Summary |
|---|---|
| [createToken](create-token.md) | [jvm]<br>fun [createToken](create-token.md)(username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>Создать JWT токен, содержащий имя пользователя как Subject |
| [login](login.md) | [jvm]<br>suspend fun [login](login.md)(request: [AuthRequest](../../ru.itmo.dto.api/-auth-request/index.md)): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>Авторизировать существующего пользователя |
| [register](register.md) | [jvm]<br>suspend fun [register](register.md)(request: [AuthRequest](../../ru.itmo.dto.api/-auth-request/index.md)): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>Зарегистировать нового пользователя |