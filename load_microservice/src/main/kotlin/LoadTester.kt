import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.coroutineContext
import kotlin.random.Random

class LoadTester(
    private val client: HttpClient,
    private val config: Config,
    private val baseUrl: String = "http://entry:8090/api/v1",
) {
    private val json = Json { ignoreUnknownKeys = true }
    private val templates: List<RequestTemplate> = loadTemplates()
    private val registrationTemplate = templates.firstOrNull { it.address == "/auth/register" }
    private val otherTemplates = templates.filter { it.address != "/auth/register" }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private fun loadTemplates(): List<RequestTemplate> {
        val resource = object {}.javaClass
            .classLoader
            .getResource("requests.json")
            ?: throw IllegalStateException("requests.json not found in resources")

        val text = resource.readText()
        val allTemplates = json.decodeFromString<List<RequestTemplate>>(text)

        val requiredAddresses = config.includeRequests.toMutableList().apply {
            if ("/auth/register" !in this) {
                add("/auth/register")
            }
        }
        return allTemplates.filter { it.address in requiredAddresses }
    }




    fun start() {
        repeat(config.userCount) {
            scope.launch { simulateUser() }
        }
        println("Запущена симуляция ${config.userCount} пользователей на $baseUrl")
    }

    fun stop() {
        scope.cancel()
        println("Симуляция остановлена")
    }

    private suspend fun simulateUser() {
        val delayMs = if (config.userDelayMs <= 0) 1 else config.userDelayMs
        delay(Random.nextLong(0, (config.userCount * 100).toLong()))

        val username = generateRandomString(8)
        val password = generateRandomString(12)

        if (registrationTemplate == null) {
            println("нет запроса на регистрацию в includeRequests");
            return
        }

        val regBody = formatBody(registrationTemplate.body, listOf(username, password))
        var token: String? = null
        try {
            var response = sendRequest(registrationTemplate, username, password, null)
            val regObj = response?.let { json.decodeFromString<RegisterResponse>(it.bodyAsText()) }
            if (regObj != null) {
                token = regObj.token ?: throw IllegalStateException("Token missing in response")
            } else {
                println("Тело ответа пустое, токен не найдет")
            }
        } catch (e: Exception) {
            println("Ошибка при ${registrationTemplate.method.uppercase()} ${registrationTemplate.address}: ${e.message}")
            return
        }
        if (token == null) {
            return
        }
        if (config.randomOrder) {
            while (coroutineContext.isActive) {
                delay(Random.nextLong(config.userDelayMs - 100, config.userDelayMs + 100))
                val tmpl = otherTemplates.random()
                sendRequest(tmpl, username, password, token)
            }
        } else {
            while (coroutineContext.isActive) {
                for (tmpl in otherTemplates) {
                    if (!coroutineContext.isActive) break
                    delay(Random.nextLong(10, 100))
                    sendRequest(tmpl, username, password, token)
                }
            }
        }
    }

    private fun printRequest(template: RequestTemplate, body: String) {
        println("${template.method.uppercase()} ${template.address}")
        println(body)
    }

    private suspend fun printResponse(response: HttpResponse) {
        println(response.status)
        println(response.bodyAsText())
    }

    private suspend fun sendRequest(
        tmpl: RequestTemplate,
        username: String,
        password: String,
        token: String?
    ): HttpResponse? {
        val args = tmpl.args.map { generateRandomValue(it, username, password) }
        val body = formatBody(tmpl.body, args)
        return try {
            printRequest(tmpl, body)
            val response: HttpResponse = client.request {
                url("$baseUrl${tmpl.address}")
                method = HttpMethod.parse(tmpl.method)
                contentType(ContentType.Application.Json)
                tmpl.headers?.forEach { (k, v) -> header(k, v) }
                if (token != null) {
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
                if (body.isNotBlank()) setBody(body)
            }
            printResponse(response)
            response
        } catch (e: Exception) {
            println("Ошибка при ${tmpl.method.uppercase()} ${tmpl.address}: ${e.message}")
            null
        }
    }


    private fun formatBody(template: String, args: List<String>): String {
        var result = template
        args.forEachIndexed { idx, value ->
            val placeholder = Regex("""\$(\d+)""")  // Ищем $1, $2, ..., $10, $11 и т.д.
            result = result.replace(placeholder) { matchResult ->
                val match = matchResult.value
                val index = match.substring(1).toInt() - 1 // Извлекаем число, сдвигаем на 1 для индекса
                if (index in args.indices) args[index] else match
            }
        }
        return result
    }



    private fun generateRandomValue(typeSpec: String, username: String, password: String): String {
        val parts = typeSpec.split(":")
        return when (parts[0].lowercase()) {
            "string" -> "\"${generateRandomString(8)}\""
            "number" -> Random.nextInt(1, 100).toString()
            "boolean" -> Random.nextBoolean().toString()
            "float" -> {
                val (min, max) = if (parts.size == 3) {
                    val minVal = parts[1].toDoubleOrNull() ?: 0.0
                    val maxVal = parts[2].toDoubleOrNull() ?: 1.0
                    minVal to maxVal
                } else {
                    0.0 to 1.0
                }
                val value = Random.nextDouble(min, max)
                value.toString()
            }
            "date" -> {
                val format = parts.getOrNull(1) ?: "yyyy-MM-dd"
                "\"${LocalDate.now().format(DateTimeFormatter.ofPattern(format))}\""
            }
            "time" -> {
                val hour = Random.nextInt(0, 24).toString().padStart(2, '0')
                val minute = Random.nextInt(0, 60).toString().padStart(2, '0')
                "\"$hour:$minute:00\""
            }
            "username" -> "\"$username\""
            "password" -> "\"$password\""
            "enum" -> {
                if (parts.size > 1) {
                    val options = parts.drop(1)
                    "\"${options.random()}\""
                } else {
                    "\"Null\""
                }
            }
            "array" -> {
                when (parts.getOrNull(1)?.lowercase()) {
                    "sleepdata" -> generateRandomSleepData()
                    else -> "[]"
                }
            }
            else -> "null"
        }
    }

    private fun generateRandomSleepData(): String {
        val phases = listOf("AWAKE", "DROWSY", "LIGHT", "DEEP", "REM")
        val now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0)

        val entries = List(4) { index ->
            val timestamp = now.plusMinutes(30L * index)
            val pulse = Random.nextInt(55, 75)
            val sleepPhase = phases.random()

            """
        {
            "timestamp": "${timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}",
            "pulse": $pulse,
            "sleepPhase": "$sleepPhase"
        }
        """.trimIndent()
        }

        return "[${entries.joinToString(",")}]"
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