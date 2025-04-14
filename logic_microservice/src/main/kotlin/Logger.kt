package ru.itmo

import org.slf4j.LoggerFactory

class Logger {
    companion object {
        private val logger = LoggerFactory.getLogger("LogicMicroservice")

        fun info(message: String) {
            logger.info(message)
        }

        fun error(message: String, exception: Throwable? = null) {
            logger.error(message, exception)
        }

        fun debug(message: String) {
            logger.debug(message)
        }
    }
}