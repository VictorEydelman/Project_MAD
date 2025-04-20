import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.JedisPubSub
import java.util.UUID

/**
 * Класс KeyDBClient реализует коммуникацию через KeyDB/Redis для обмена запросами и ответами.
 *
 * Использует JedisPool для потокобезопасного доступа к Redis.
 */
class KeyDBClient(
    host: String = "keydb",
    port: Int = 6379,
    poolConfig: JedisPoolConfig = defaultPoolConfig()
) {
    private val pool = JedisPool(poolConfig, host, port)
    private val objectMapper = jacksonObjectMapper().apply {
        registerModules(JavaTimeModule())
    }

    companion object {
        private fun defaultPoolConfig(): JedisPoolConfig {
            return JedisPoolConfig().apply {
                maxTotal = 128
                maxIdle = 16
                minIdle = 8
                testOnBorrow = true
                testWhileIdle = true
                blockWhenExhausted = true
            }
        }
    }

    /**
     * Отправляет запрос через указанный канал и ожидает ответ.
     */
    suspend fun <TRequest : Any, TResponse : Any> sendRequest(
        channel: String,
        message: TRequest,
        responseType: Class<TResponse>,
        timeoutMs: Long = 5000
    ): TResponse? {
        val json = objectMapper.writeValueAsString(message)
        val responseJson = sendRequest(channel, json, timeoutMs)
        return responseJson?.let { objectMapper.readValue(it, responseType) }
    }

    /**
     * Отправляет запрос как JSON-строку, ожидает ответ по ключу в Redis.
     */
    suspend fun sendRequest(
        channel: String,
        message: String,
        timeoutMs: Long = 5000
    ): String? {
        val uid = UUID.randomUUID().toString()
        val responseKey = "response:$channel:$uid"
        val requestMessage = UIDMessage(uid, message)

        publish(channel, requestMessage)

        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < timeoutMs) {
            pool.resource.use { jedis ->
                jedis.get(responseKey)?.let { resp ->
                    jedis.del(responseKey)
                    return resp
                }
            }
            delay(50)
        }
        return null
    }

    /**
     * Подписывается на указанный канал для приёма запросов и отправки ответов.
     */
    suspend fun subscribeWithResponse(
        channel: String,
        messageHandler: (String) -> String,
        responseTTL: Long = 10
    ) {
        subscribe(
            channel,
            onMessage = { message ->
                val request: UIDMessage = objectMapper.readValue(message)
                val response = messageHandler(request.message)
                publishResponse(channel, request.uid, response, responseTTL)
            },
            repeat = true
        )
    }

    suspend fun <TRequest : Any, TResponse: Any> subscribeWithResponse(
        channel: String,
        requestType: Class<TRequest>,
        messageHandler: (TRequest) -> TResponse?,
        responseTTL: Long = 10
    ) {
        subscribeWithResponse(channel, { requestJson ->
            // Десериализуем запрос в тип TRequest и вызываем обработчик.
            objectMapper.writeValueAsString(
                messageHandler(objectMapper.readValue(requestJson, requestType)))
        }, responseTTL)
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun subscribe(
        channel: String,
        onMessage: (String) -> Unit,
        repeat: Boolean = false
    ) {
        println("Subscribing to channel: $channel")
        withContext(Dispatchers.IO) {
            pool.resource.use { jedis ->
                jedis.subscribe(object : JedisPubSub() {
                    override fun onMessage(ch: String, msg: String) {
                        println("Message on $ch: $msg")
                        if (!repeat) unsubscribe()
                        onMessage(msg)
                    }
                }, channel)
            }
        }
    }

    suspend fun <TMessage : Any> subscribe(
        channel: String,
        onMessage: (TMessage) -> Unit,
        messageType: Class<TMessage>,
        repeat: Boolean = false
    ) {
        subscribe(channel, { msg -> onMessage(objectMapper.readValue(msg, messageType)) }, repeat)
    }

    /**
     * Публикует сообщение в указанный канал через пул соединений.
     */
    fun publish(channel: String, message: Any) {
        val json = objectMapper.writeValueAsString(message)
        println("Publishing to $channel: $json")
        pool.resource.use { jedis -> jedis.publish(channel, json) }
    }

    /**
     * Публикует ответ на запрос в Redis с указанным TTL.
     */
    private fun publishResponse(
        channel: String,
        uid: String,
        response: String,
        ttl: Long
    ) {
        val responseKey = "response:$channel:$uid"
        println("Publishing response to $responseKey: $response (TTL $ttl)")
        pool.resource.use { jedis -> jedis.setex(responseKey, ttl, response) }
    }

    fun <T : Any> pop(queue: String, type: Class<T>): T? =
        pool.resource.use { jedis ->
            jedis.rpop(queue)?.let { objectMapper.readValue(it, type) }
        }

    fun <T : Any> get(key: String, type: Class<T>): T? =
        pool.resource.use { jedis ->
            jedis.get(key)?.let { objectMapper.readValue(it, type) }
        }

    fun push(queue: String, value: Any) =
        pool.resource.use { jedis ->
            jedis.lpush(queue, objectMapper.writeValueAsString(value))
        }

    fun set(key: String, value: Any) =
        pool.resource.use { jedis ->
            jedis.set(key, objectMapper.writeValueAsString(value))
        }

    fun close() = pool.close()
}

data class UIDMessage(val uid: String, val message: String)
