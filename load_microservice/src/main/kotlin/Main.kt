import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Config(
    val userCount: Int,
    val initialDelayMs: Long,
    val userDelayMs: Long,
    val randomOrder: Boolean,
    val includeRequests: List<String>
)

fun main() = runBlocking {
    val configText = object {}.javaClass
        .classLoader
        .getResource("config.json")
        ?.readText()
        ?: throw IllegalStateException("config.json not found in resources")

    val config = Json { ignoreUnknownKeys = true }
        .decodeFromString(Config.serializer(), configText)

    val client = HttpClient(CIO)
    val tester = LoadTester(client, config)

    if (config.initialDelayMs > 0) {
        println("Тест начнётся через ${config.initialDelayMs} мс...")
    }
    delay(config.initialDelayMs)

    tester.start()

    println("Нажмите ENTER для остановки...")
    readLine()

    tester.stop()
    client.close()
}
