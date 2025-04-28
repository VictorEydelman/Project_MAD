package ru.itmo.keydb

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
        keydb.publish("log", request)
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
