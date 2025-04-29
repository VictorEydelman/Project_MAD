//[entry_microservice](../../../index.md)/[ru.itmo.service](../index.md)/[AuthService](index.md)/[login](login.md)

# login

[jvm]\
suspend fun [login](login.md)(request: [AuthRequest](../../ru.itmo.dto.api/-auth-request/index.md)): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)

Авторизировать существующего пользователя

#### Return

JWT токен

#### Parameters

jvm

| | |
|---|---|
| request | Объект с именем пользователя и паролем |