import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.*
import kotlinx.coroutines.launch
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPooled
import redis.clients.jedis.JedisPubSub
import java.util.UUID

object KeyDBClient {
    private lateinit var jedisPub: Jedis // Публикация
    private lateinit var jedisSub: JedisPooled // Подписка
    private val objectMapper = jacksonObjectMapper()

    fun init(host: String, port: Int) {
        jedisPub = Jedis(host, port)
        jedisSub = JedisPooled(host, port)
    }

    /**
     * Отправляет запрос через канал и ожидает ответ.
     * @param channel Канал для отправки запроса
     * @param message Сообщение, отправляемое в канал
     * @return Результат ответа
     */
    suspend inline fun <TRequest: Any, reified TResponse: Any?> sendAwaitRequest(
        channel: String,
        message: TRequest
    ): TResponse {
        val deferred = CompletableDeferred<TResponse>()
        sendRequest(channel, message, TResponse::class.java) {
            deferred.complete(it)
        }
        return deferred.await()
    }

    /**
     * Отправляет запрос через канал и ожидает ответ.
     * @param channel Канал для отправки запроса
     * @param message Сообщение, отправляемое в канал
     * @param messageHandler Функция обработки ответа
     * @param responseType Класс типа ответа
     */
    suspend fun <TRequest: Any, TResponse: Any?> sendRequest(
        channel: String,
        message: TRequest,
        responseType: Class<TResponse>,
        messageHandler: (TResponse) -> Unit
    ) {
        sendStringRequest(channel, objectMapper.writeValueAsString(message)) { responseJson ->
            messageHandler(objectMapper.readValue(responseJson, responseType))
        }
    }

    /**
     * Отправляет запрос через канал и ожидает ответ.
     * @param channel Канал для отправки запроса
     * @param message Сообщение, отправляемое в канал
     * @param messageHandler Функция обработки ответа
     */
    suspend fun sendStringRequest(
        channel: String,
        message: String,
        messageHandler: (String) -> Unit
    ) {
        val uid = UUID.randomUUID().toString()
        val responseChannel = "response:$channel:$uid"
        val requestMessage = UIDMessage(uid, message)

        publish(channel, requestMessage)
        subscribe(responseChannel, false, messageHandler)
    }

    /**
     * Подписывается на канал, принимает запросы и отправляет ответы.
     * @param channel Канал подписки
     * @param messageHandler Функция обработки полученного запроса
     */
    @OptIn(DelicateCoroutinesApi::class)
    inline fun <reified TRequest: Any, TResponse: Any?> receiveRequest(
        channel: String,
        repeat: Boolean = false,
        noinline messageHandler: (TRequest) -> TResponse
    ) {
        GlobalScope.launch {
            receiveRequest(channel, TRequest::class.java, repeat, messageHandler)
        }
    }

    /**
     * Подписывается на канал, принимает запросы и отправляет ответы.
     * @param channel Канал подписки
     * @param messageHandler Функция обработки полученного запроса
     * @param requestType Класс типа запроса
     */
    suspend fun <TRequest: Any, TResponse: Any?> receiveRequest(
        channel: String,
        requestType: Class<TRequest>,
        repeat: Boolean = false,
        messageHandler: (TRequest) -> TResponse
    ) {
        receiveStringRequest(channel, repeat) { message ->
            val requestData: TRequest = objectMapper.readValue(message, requestType)
            messageHandler(requestData)
        }
    }

    /**
     * Подписывается на канал, принимает запросы и отправляет ответы.
     * @param channel Канал подписки
     * @param messageHandler Функция обработки полученного запроса
     */
    suspend fun receiveStringRequest(
        channel: String,
        repeat: Boolean = false,
        messageHandler: (String) -> Any?
    ) {
        subscribe(channel, repeat) { message ->
            val request: UIDMessage = objectMapper.readValue(message)
            val response = messageHandler(request.message)
            publish("response:$channel:${request.uid}", response)
        }
    }


    /**
     * Подписывается на указанный канал и вызывает переданный обработчик при получении сообщения.
     * @param channel Канал подписки
     * @param onMessage Функция обработки полученного сообщения
     */
    suspend fun subscribe(channel: String, repeat: Boolean = false, onMessage: (String) -> Unit) {
        println("Subscribing to channel: $channel")
        withContext(Dispatchers.IO) {
            jedisSub.subscribe(object : JedisPubSub() {
                override fun onMessage(channel: String, message: String) {
                    println("Message received: $message")
                    if (!repeat) unsubscribe()
                    onMessage(message)
                }
            }, channel)
        }
    }

    /**
     * Публикует сообщение в указанный канал.
     * @param channel Канал для публикации
     * @param message Сообщение для публикации
     */
    fun publish(channel: String, message: Any?) {
        val jsonMessage = objectMapper.writeValueAsString(message)
        println("Publishing to $channel: $jsonMessage")
        jedisPub.publish(channel, jsonMessage)
    }

    /**
     * Извлекает последний элемент из очереди.
     * @param queue Очередь для извлечения
     * @param type Класс типа данных
     * @return Полученный объект или null
     */
    fun <T: Any> pop(queue: String, type: Class<T>): T? = jedisSub.rpop(queue)?.let { objectMapper.readValue(it, type) }

    /**
     * Получает значение из хранилища по ключу.
     * @param queue Ключ хранилища
     * @param type Класс типа данных
     * @return Полученный объект или null
     */
    fun <T: Any> get(queue: String, type: Class<T>): T? = jedisSub.get(queue)?.let { objectMapper.readValue(it, type) }

    /**
     * Добавляет элемент в начало очереди.
     * @param queue Очередь для добавления
     * @param value Значение для добавления
     */
    fun push(queue: String, value: Any) = jedisPub.lpush(queue, objectMapper.writeValueAsString(value))

    /**
     * Устанавливает значение в хранилище.
     * @param queue Ключ хранилища
     * @param value Значение для сохранения
     */
    fun set(queue: String, value: Any) = jedisPub.set(queue, objectMapper.writeValueAsString(value))

    /**
     * Закрывает соединения с KeyDB.
     */
    fun close() {
        jedisPub.close()
        jedisSub.close()
    }
}

/**
 * Класс для передачи сообщений с UID.
 */
data class UIDMessage(val uid: String, val message: String)