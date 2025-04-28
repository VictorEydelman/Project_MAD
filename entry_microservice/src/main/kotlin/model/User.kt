package ru.itmo.model

/**
 * Пользователь
 * @param username Имя пользователя. Должно быть не менее 4 символов, может включать только символы [a-zA-Z0-9_]
 * @param password Пароль. Должен быть не менее 4 символов
 */
data class User(
    val username: String,
    val password: String,
)
