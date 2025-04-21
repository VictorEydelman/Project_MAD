import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.coroutines.coroutineContext
import kotlin.random.Random

class LoadTester(
    private val client: HttpClient,
    private val userCount: Int = 500,
    private val baseUrl: String = "http://entry:8090",
    private val startupDelayMs: Long = 5_000L
) {
    private val json = Json { ignoreUnknownKeys = true }
    private val templates: List<RequestTemplate> = loadTemplates()
    private val registrationTemplate = templates.first()
    private val otherTemplates = templates.drop(1)

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private fun loadTemplates(): List<RequestTemplate> {
        val file = File("resources/main/requests.json")
        val text = file.readText()
        return json.decodeFromString(file.readText())
    }

    fun start() {
        repeat(userCount) {
            scope.launch { simulateUser() }
        }
        println("Запущена симуляция $userCount пользователей на $baseUrl")
    }

    fun stop() {
        scope.cancel()
        println("Симуляция остановлена")
    }

    private suspend fun simulateUser() {
        delay(Random.nextLong(0, startupDelayMs))

        val username = generateRandomString(8)
        val password = generateRandomString(12)

        // Регистрация
        val regBody = formatBody(registrationTemplate.body, listOf(username, password))
        var regText: String = ""
        val token: String
        try {
            val regResponse: HttpResponse = client.request {
                url("$baseUrl${registrationTemplate.address}")
                method = HttpMethod.parse(registrationTemplate.method)
                contentType(ContentType.Application.Json)
                setBody(regBody)
            }
            println(regResponse)
            regText = regResponse.bodyAsText()
            // Пытаемся получить токен из тела ответа
            val regObj = json.decodeFromString<RegisterResponse>(regText)
            token = regObj.token ?: throw IllegalStateException("Token missing in response")
        } catch (e: Exception) {
            println("[${Thread.currentThread().name}] Ошибка регистрации: ${e.message}")
            println("Ответ сервера: " + (runCatching { regText }.getOrNull() ?: "неизвестен"))
            return
        }

        // Основные запросы
        while (coroutineContext.isActive) {
            delay(Random.nextLong(10, 100))
            val tmpl = otherTemplates.random()
            if (tmpl.address == "/api/v1/sleep/calculate-recommended-asleep-time") {
                println("huge")
            }
            val args = tmpl.args.map { generateRandomValue(it, username, password) }
            val body = formatBody(tmpl.body, args)
            try {
                val response:HttpResponse = client.request {
                    url("$baseUrl${tmpl.address}")
                    method = HttpMethod.parse(tmpl.method)
                    contentType(ContentType.Application.Json)
                    tmpl.headers?.forEach { (k, v) -> header(k, v) }
                    header(HttpHeaders.Authorization, "Bearer $token")
                    if (body.isNotBlank()) setBody(body)
                }
                println("[${Thread.currentThread().name}] ${tmpl.method.uppercase()} ${tmpl.address} -> ${response.status}\n${response.bodyAsText()}")
            } catch (e: Exception) {
                println("[${Thread.currentThread().name}] Ошибка при ${tmpl.method.uppercase()} ${tmpl.address}: ${e.message}")
            }
        }
    }

    private fun formatBody(template: String, args: List<String>): String {
        var result = template
        args.forEachIndexed { idx, value ->
            val placeholder = Regex("""\$${idx + 1}""")  // Ищем $1, $2 и т.д.
            result = result.replace(placeholder, "\"" + value + "\"")
        }
        return result
    }


    private fun generateRandomValue(typeSpec: String, username: String, password: String): String {
        val parts = typeSpec.split(":", limit = 2)
        return when (parts[0].lowercase()) {
            "string" -> "${generateRandomString(8)}"
            "number" -> Random.nextInt(1, 100).toString()
            "boolean" -> Random.nextBoolean().toString()
            "float" -> Random.nextDouble().toString()
            "date" -> {
                val format = parts.getOrNull(1) ?: "yyyy-MM-dd"
                "${LocalDate.now().format(DateTimeFormatter.ofPattern(format))}"
            }
            "user" -> username
            "password" -> password
            else -> "\"null\""
        }
    }

    private fun generateRandomString(length: Int): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    @kotlinx.serialization.Serializable
    private data class RegisterResponse(val token: String? = null)
}