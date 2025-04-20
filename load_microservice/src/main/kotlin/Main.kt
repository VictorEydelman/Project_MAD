import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Paths

@Serializable
data class Config(
    val userCount: Int,
    val delayMs: Long
)

fun main() = runBlocking {
    // 1. Читаем файл из classpath
    val configText = object {}.javaClass
        .classLoader
        .getResource("config.json")
        ?.readText()
        ?: throw IllegalStateException("config.json not found in resources")

    // 2. Парсим JSON в Config
    val config = Json { ignoreUnknownKeys = true }
        .decodeFromString(Config.serializer(), configText)

    // 3. Используем параметры из конфигурации
    val client = HttpClient(CIO)
    val tester = LoadTester(client, userCount = config.userCount)

    println("Тест начнётся через ${config.delayMs} мс...")
    delay(config.delayMs)

    tester.start()

    println("Нажмите ENTER для остановки...")
    readLine()

    tester.stop()
    client.close()
}
