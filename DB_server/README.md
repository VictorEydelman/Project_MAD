# DB_server

Микросервис для подключения к бд postgres и clickhouse.

Для внешних пользователей предоставляет:

Таблица User:

save-user - запрос который принимает Users(val username: String, val password: String) и возвращает Boolean

get-user - запрос который принимает username: String и возвращает Users(val username: String, val password: String)

user-not-exist - запрос который принимает username: String и возвращает Boolean, если пользователя нету то false, иначе true



