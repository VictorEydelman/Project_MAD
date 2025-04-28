package ru.itmo

import LoggerRequestDTO
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import org.slf4j.LoggerFactory


object Logging {
    private val loggers = HashMap<String, KLogger>()
    private var filename = "LoggerMicroservice"

    fun getLogger(name: String): KLogger {
        return loggers.computeIfAbsent(name) {
            KotlinLogging.logger(name)
        }
    }

    fun log(name: String, message: String, level: Level = Level.INFO) {
        changeFilename(name)
        getLogger(name).at(level) {
            this.message = message
        }
    }

    fun handle(request: LoggerRequestDTO) {
        var message = request.message
        request.stacktrace?.let { message = "$message\n$it" }
        log(request.name, message, request.level)
    }

    fun info(message: String) {
        log("LoggerMicroservice", message, Level.INFO)
    }

    private fun changeFilename(filename: String) {
        if (filename == this.filename) return
        val context = LoggerFactory.getILoggerFactory() as LoggerContext
        val configurator = JoranConfigurator()
        configurator.context = context
        context.reset()
        context.putProperty("filename", filename)
        configurator.doConfigure(javaClass.classLoader.getResourceAsStream("logback.xml"))
        this.filename = filename
    }

}