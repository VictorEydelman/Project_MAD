import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KeyDBClient(
    private val host: String = "keydb",
    private val port: Int = 6379
) {
    private val jedisPub: Jedis by lazy { Jedis(host, port) }  // Публикация
    private val jedisSub: Jedis by lazy { Jedis(host, port) }  // Подписка
    private val objectMapper = jacksonObjectMapper()

    suspend fun <TRequest: Any, TResponse: Any> sendRequest(
        channel: String,
        message: TRequest,
        messageHandler: (TResponse) -> Unit,
        responseType: Class<TResponse>
    ) {
        val uid = UUID.randomUUID().toString()
        val responseChannel = "response:$channel:$uid"
        val requestMessage = UIDMessage(uid, objectMapper.writeValueAsString(message))

        publish(channel, requestMessage)

        subscribe(responseChannel) { message ->
            messageHandler(objectMapper.readValue(message, responseType))
        }
    }

    suspend fun <TRequest: Any, TResponse: Any> receiveRequest(
        channel: String,
        messageHandler: (TRequest) -> TResponse,
        requestType: Class<TRequest>
    ) {
        subscribe(channel) { message ->
            val request: UIDMessage = objectMapper.readValue(message)
            val requestData: TRequest = objectMapper.readValue(request.message, requestType)
            val uid = request.uid
            val response = messageHandler(requestData)
            publish("response:$channel:$uid", response)
        }
    }

    suspend fun subscribe(channel: String, onMessage: (String) -> Unit) {
        println("Subscribing to channel: $channel")
        withContext(Dispatchers.IO) {
            jedisSub.subscribe(object : JedisPubSub() {
                override fun onMessage(channel: String, message: String) {
                    println("Message received: $message")
                    unsubscribe()
                    onMessage(message)
                }
            }, channel)
        }
    }

    fun publish(channel: String, message: Any) {
        val jsonMessage = objectMapper.writeValueAsString(message)
        println("Publishing to $channel: $jsonMessage")
        jedisPub.publish(channel, jsonMessage)
    }

    fun <T: Any> pop(queue: String, type: Class<T>): T? = jedisSub.rpop(queue)?.let { objectMapper.readValue(it, type) }
    fun <T: Any> get(queue: String, type: Class<T>): T? = jedisSub.get(queue)?.let { objectMapper.readValue(it, type) }
    fun push(queue: String, value: Any) = jedisPub.lpush(queue, objectMapper.writeValueAsString(value))
    fun set(queue: String, value: Any) = jedisPub.set(queue, objectMapper.writeValueAsString(value))

    fun close() {
        jedisPub.close()
        jedisSub.close()
    }
}

data class UIDMessage(val uid: String, val message: String)