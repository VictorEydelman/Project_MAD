package ru.itmo

import io.ktor.client.*
import kotlin.system.exitProcess


fun main() {
    val tester = LoadTester(HttpClient())

    println("Введите команду (start / stop / exit):")
    while (true) {
        when (readlnOrNull()?.trim()) {
            "start" -> tester.startLoadTesting()
            "stop" -> tester.stopLoadTesting()
            "exit" -> {
                tester.stopLoadTesting()
                exitProcess(0)
            }
            else -> println("Неизвестная команда. Используйте: start / stop / exit.")
        }
    }
}
