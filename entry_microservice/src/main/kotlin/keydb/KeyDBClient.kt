import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub
import java.util.UUID

/**
 * Класс KeyDBClient реализует коммуникацию через KeyDB/Redis для обмена запросами и ответами.
 *
 * Основной механизм – отправка запроса через публикацию сообщения в канал,
 * а затем получение ответа, который сохраняется в Redis под уникальным ключом с TTL.
 *
 * Также класс предоставляет методы для подписки на каналы (с возможностью одноразовой или повторяемой подписки)
 * и для работы с очередями и ключами в хранилище.
 */
class KeyDBClient(
    private val host: String = "keydb",
    private val port: Int = 6379
) {
    // Глобальный экземпляр Jedis для публикации и выполнения команд.
    private val jedis: Jedis by lazy { Jedis(host, port) }
    private val objectMapper = jacksonObjectMapper()
    init {
        objectMapper.registerModules(JavaTimeModule())
    }

    /**
     * Отправляет запрос через указанный канал и ожидает ответ.
     *
     * Запрос сериализуется в JSON, публикуется с уникальным идентификатором (uid).
     * Ответ должен быть записан в Redis под ключом "response:<channel>:<uid>".
     * Клиент опрашивает этот ключ до получения ответа или истечения таймаута.
     *
     * @param channel Канал для отправки запроса.
     * @param message Объект запроса, который будет сериализован в JSON.
     * @param responseType Класс, в который будет десериализован ответ.
     * @param timeoutMs Таймаут ожидания ответа в миллисекундах (по умолчанию 5000).
     * @return Ответ типа TResponse или null, если ответ не получен вовремя.
     */
    suspend fun <TRequest : Any, TResponse : Any> sendRequest(
        channel: String,
        message: TRequest,
        responseType: Class<TResponse>,
        timeoutMs: Long = 5000
    ): TResponse? {
        val responseJson = sendRequest(channel, objectMapper.writeValueAsString(message), timeoutMs)
        return if (responseJson != null) {
            objectMapper.readValue(responseJson, responseType)
        } else {
            null
        }
    }

    /**
     * Отправляет запрос через указанный канал и ожидает ответ.
     *
     * Запрос передаётся в виде JSON-строки. Ответ ожидается через опрос Redis по ключу "response:<channel>:<uid>".
     *
     * @param channel Канал для отправки запроса.
     * @param message Сообщение (JSON-строка) запроса.
     * @param timeoutMs Таймаут ожидания ответа в миллисекундах (по умолчанию 5000).
     * @return JSON-строка ответа или null, если ответ не получен вовремя.
     */
    suspend fun sendRequest(
        channel: String,
        message: String,
        timeoutMs: Long = 5000
    ): String? {
        val uid = UUID.randomUUID().toString()
        val responseKey = "response:$channel:$uid"
        val requestMessage = UIDMessage(uid, message)

        // Публикуем запрос в канал.
        publish(channel, requestMessage)

        // Ожидаем появления ответа в Redis по ключу responseKey.
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < timeoutMs) {
            val responseJson = jedis.get(responseKey)
            if (responseJson != null) {
                jedis.del(responseKey)
                return responseJson
            }
            delay(50)
        }
        return null
    }

    /**
     * Подписывается на указанный канал для приёма запросов и отправки ответов.
     *
     * При получении сообщения (запроса) оно десериализуется в объект UIDMessage.
     * Затем вызывается функция messageHandler, которая обрабатывает текст запроса и возвращает ответ в виде строки.
     * Ответ сохраняется в Redis под ключом "response:<channel>:<uid>" с заданным TTL.
     *
     * @param channel Канал подписки.
     * @param messageHandler Функция обработки запроса (текст запроса) и генерации ответа.
     * @param responseTTL Время жизни ключа с ответом в секундах (по умолчанию 10).
     */
    suspend fun subscribeWithResponse(
        channel: String,
        messageHandler: (String) -> String,
        responseTTL: Long = 10
    ) {
        subscribe(channel, { message ->
            val request: UIDMessage = objectMapper.readValue(message)
            val response = messageHandler(request.message)
            publishResponse(channel, request.uid, response, responseTTL)
        }, repeat = true)
    }

    /**
     * Подписывается на указанный канал для приёма запросов и отправки ответов.
     *
     * Запрос десериализуется в объект типа TRequest, после чего вызывается messageHandler,
     * возвращающий объект-ответ, который сериализуется в JSON и сохраняется в Redis с заданным TTL.
     *
     * @param channel Канал подписки.
     * @param messageHandler Функция обработки запроса типа TRequest и генерации ответа типа TResponse.
     * @param requestType Класс типа запроса TRequest.
     * @param responseTTL Время жизни ключа с ответом в секундах (по умолчанию 10).
     */
    suspend fun <TRequest : Any> subscribeWithResponse(
        channel: String,
        messageHandler: (TRequest) -> String,
        requestType: Class<TRequest>,
        responseTTL: Long = 10
    ) {
        subscribeWithResponse(channel, { requestJson ->
            // Десериализуем запрос в тип TRequest и вызываем обработчик.
            messageHandler(objectMapper.readValue(requestJson, requestType))
        }, responseTTL)
    }

    /**
     * Подписывается на указанный канал и вызывает переданный обработчик при получении сообщения.
     *
     * Для подписки создаётся временное соединение (новый экземпляр Jedis),
     * чтобы не блокировать глобальный экземпляр для публикации и других команд.
     *
     * @param channel Канал подписки.
     * @param onMessage Функция обработки полученного сообщения (в формате JSON-строки).
     * @param repeat Если false, подписка отписывается после первого сообщения; если true, остается активной.
     */
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun subscribe(channel: String, onMessage: (String) -> Unit, repeat: Boolean = false) {
        println("Subscribing to channel: $channel")
        withContext(Dispatchers.IO) {
            // Создаем временное соединение для подписки
            Jedis(host, port).use { tempJedis ->
                tempJedis.subscribe(object : JedisPubSub() {
                    override fun onMessage(ch: String, message: String) {
                        println("Message received on channel $ch: $message")
                        if (!repeat) unsubscribe()
                        onMessage(message)
                    }
                }, channel)
            }
        }
    }

    /**
     * Перегруженный метод подписки, который автоматически десериализует входящее сообщение в заданный тип.
     *
     * @param channel Канал подписки.
     * @param onMessage Функция обработки сообщения типа TMessage.
     * @param messageType Класс типа TMessage.
     * @param repeat Если false, подписка завершится после первого сообщения; если true – останется активной.
     */
    suspend fun <TMessage : Any> subscribe(
        channel: String,
        onMessage: (TMessage) -> Unit,
        messageType: Class<TMessage>,
        repeat: Boolean = false
    ) {
        subscribe(channel, { responseString ->
            onMessage(objectMapper.readValue(responseString, messageType))
        }, repeat)
    }

    /**
     * Публикует сообщение в указанный канал.
     *
     * Сообщение сериализуется в JSON и отправляется через глобальный экземпляр Jedis.
     *
     * @param channel Канал для публикации.
     * @param message Сообщение (любой объект, сериализуемый в JSON).
     */
    fun publish(channel: String, message: Any) {
        val jsonMessage = objectMapper.writeValueAsString(message)
        println("Publishing to $channel: $jsonMessage")
        jedis.publish(channel, jsonMessage)
    }

    /**
     * Публикует ответ на запрос в Redis с указанным TTL.
     *
     * Ответ сохраняется под ключом "response:<channel>:<uid>".
     *
     * @param channel Канал запроса.
     * @param uid Уникальный идентификатор запроса.
     * @param response Ответ (в виде строки), который будет сохранён.
     * @param ttl Время жизни ключа с ответом в секундах.
     */
    private fun publishResponse(channel: String, uid: String, response: String, ttl: Long) {
        val responseKey = "response:$channel:$uid"
        println("Publishing response to key $responseKey: $response (TTL: $ttl sec)")
        jedis.setex(responseKey, ttl, response)
    }

    /**
     * Извлекает последний элемент из очереди.
     *
     * @param queue Название очереди (ключа).
     * @param type Класс типа данных, в который будет десериализован элемент.
     * @return Объект типа T или null, если очередь пуста.
     */
    fun <T : Any> pop(queue: String, type: Class<T>): T? =
        jedis.rpop(queue)?.let { objectMapper.readValue(it, type) }

    /**
     * Получает значение из хранилища по ключу.
     *
     * @param queue Ключ хранилища.
     * @param type Класс типа данных, в который будет десериализовано значение.
     * @return Объект типа T или null, если ключ не найден.
     */
    fun <T : Any> get(queue: String, type: Class<T>): T? =
        jedis.get(queue)?.let { objectMapper.readValue(it, type) }

    /**
     * Добавляет элемент в начало очереди.
     *
     * @param queue Название очереди.
     * @param value Объект, который будет сериализован в JSON и добавлен в очередь.
     */
    fun push(queue: String, value: Any) =
        jedis.lpush(queue, objectMapper.writeValueAsString(value))

    /**
     * Устанавливает значение в хранилище.
     *
     * @param queue Ключ хранилища.
     * @param value Объект, который будет сериализован в JSON и сохранён.
     */
    fun set(queue: String, value: Any) =
        jedis.set(queue, objectMapper.writeValueAsString(value))

    /**
     * Закрывает соединение с KeyDB.
     */
    fun close() {
        jedis.close()
    }
}

/**
 * Класс для передачи сообщений с UID.
 *
 * Объект данного типа инкапсулирует уникальный идентификатор запроса (uid)
 * и само сообщение (в виде строки, обычно JSON).
 */
data class UIDMessage(val uid: String, val message: String)