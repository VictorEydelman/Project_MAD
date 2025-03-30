package mad.project.keyDB

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub

class KeyDBClient(
    private val host: String = "keydb",
    private val port: Int = 6379
) {
    private val jedis: Jedis by lazy {
        Jedis(host, port)
    }

    // Публикация сообщения в канал

    fun publish(channel: String, message: String) {
        jedis.publish(channel, message)
    }

    // Подписка на канал
    fun subscribe(channel: String, messageHandler: (String) -> Unit) {
        Thread {
            jedis.subscribe(object : JedisPubSub() {
                override fun onMessage(channel: String, message: String) {
                    messageHandler(message)
                }
            }, channel)
        }.start()
    }

    // Добавление элемента в очередь (список)
    fun push(queue: String, value: String) {
        jedis.lpush(queue, value)
    }

    // Извлечение элемента из очереди (списка)
    fun pop(queue: String): String? {
        return jedis.rpop(queue)
    }

    // Закрытие соединения
    fun close() {
        jedis.close()
    }
}
