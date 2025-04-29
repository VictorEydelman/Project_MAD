//[entry_microservice](../../../index.md)/[ru.itmo.service](../index.md)/[AuthService](index.md)/[register](register.md)

# register

[jvm]\
suspend fun [register](register.md)(request: [AuthRequest](../../ru.itmo.dto.api/-auth-request/index.md)): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)

Зарегистировать нового пользователя

#### Return

JWT токен

#### Parameters

jvm

| | |
|---|---|
| request | Объект с именем пользователя и паролем |