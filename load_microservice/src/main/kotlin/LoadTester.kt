package ru.itmo

import io.ktor.client.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.random.Random

class LoadTester(private val client: HttpClient) {
    private val requestTemplates: List<RequestTemplate> = loadRequestTemplates()
    private var isRunning = false
    private var job: Job? = null

    private fun loadRequestTemplates(): List<RequestTemplate> {
        val file = File("resources/requests.json")
        return Json.decodeFromString(file.readText())
    }

    private fun formatBody(template: String, args: List<String>): String {
        var formatted = template
        args.forEachIndexed { index, value ->
            formatted = formatted.replace("\$${index + 1}", value)
        }
        return formatted
    }

    fun startLoadTesting() {
        if (isRunning) {
            println("Тест уже запущен!")
            return
        }
        isRunning = true
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isRunning) {
                val template = requestTemplates.random() // Выбираем случайный запрос
                val args = template.args.map { generateRandomValue(it) } // Генерация случайных аргументов
                val formattedBody = formatBody(template.body, args)
                sendRequest(template, formattedBody)
                delay(10)
            }
        }
        println("Тестирование запущено!")
    }

    fun generateRandomValue(type: String): String {
        return when (type) {
            "string" -> "\"${generateRandomString(8)}\"" // Строки заключаем в кавычки
            "number" -> Random.nextInt(1, 100).toString() // Случайное число от 1 до 100
            else -> "\"unknown\"" // Если неизвестный тип, подставляем "unknown"
        }
    }

    fun generateRandomString(length: Int): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    fun stopLoadTesting() {
        if (!isRunning) {
            println("Тест уже остановлен!")
            return
        }
        isRunning = false
        job?.cancel()
        println("Тестирование остановлено.")
    }

    private suspend fun sendRequest(template: RequestTemplate, formattedBody: String) {
        println("Отправка запроса: ${template.method.uppercase()} ${template.address} с телом $formattedBody")
        // Тут можно отправить запрос через client
    }
}