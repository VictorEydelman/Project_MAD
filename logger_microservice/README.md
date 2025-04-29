# Logger Microservice

Чтобы подключиться к Логгеру, нужно:

1. Вставить куда то класс [Logger](src/main/kotlin/Logger.kt)
2. Проинициализовать Logger с помощью KeyDB клиента и названия

Пример:

```kotlin
val keydb = KeyDBClient()
Logger.init(keydb, "EntryMicroservice") // Замените на ваше имя

Logger.info("Hello, world!")
```
Сообщение уйдет в микросервис Logger

Класс Logger для создания:
```kotlin
import KeyDBClient
import java.time.LocalTime

enum class LogLevel {
    ERROR, WARN, INFO, DEBUG, TRACE
}

data class LoggerRequest(
    val name: String,
    val message: String,
    val level: LogLevel,
    val stacktrace: String? = null,
)

object Logger {
    private lateinit var keydb: KeyDBClient
    private lateinit var name: String

    fun init(keydb: KeyDBClient, name: String) {
        this.keydb = keydb
        this.name = name
    }

    fun log(message: String, level: LogLevel, stacktrace: String? = null) {
        val request = LoggerRequest(
            name,
            message,
            level,
            stacktrace,
        )
        println("${LocalTime.now()} $level [$name] $message" + if (stacktrace != null) "\n$stacktrace" else "")
        runCatching { keydb.publish("log", request) }
    }

    fun debug(message: String) {
        log(message, LogLevel.DEBUG, null)
    }

    fun info(message: String) {
        log(message, LogLevel.INFO, null)
    }

    fun warn(message: String) {
        log(message, LogLevel.WARN, null)
    }

    fun error(message: String, throwable: Throwable? = null) {
        log(message, LogLevel.ERROR, throwable?.stackTraceToString())
    }

}
```
