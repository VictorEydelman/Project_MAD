//[entry_microservice](../../../index.md)/[ru.itmo.model](../index.md)/[User](index.md)

# User

data class [User](index.md)(val username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val password: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html))

Пользователь

#### Parameters

jvm

| | |
|---|---|
| username | Имя пользователя. Должно быть не менее 4 символов, может включать только символы a-zA-Z0-9_ |
| password | Пароль. Должен быть не менее 4 символов |

## Constructors

| | |
|---|---|
| [User](-user.md) | [jvm]<br>constructor(username: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), password: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [password](password.md) | [jvm]<br>val [password](password.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [username](username.md) | [jvm]<br>val [username](username.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |