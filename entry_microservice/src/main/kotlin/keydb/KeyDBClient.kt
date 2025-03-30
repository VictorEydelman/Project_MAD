package ru.itmo.keydb

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub

object KeyDBClient {
    private val objectMapper = jacksonObjectMapper()
    private lateinit var jedis: Jedis

    fun init(host: String, port: Int) {
        jedis = Jedis(host, port)
    }

    fun getJedis(): Jedis {
        return jedis
    }

    // Публикация сообщения в канал
    fun publish(channel: String, message: String) {
        jedis.publish(channel, message)
    }
    fun <T> publish(channel: String, request: KeyDBRequest<T>) {
        jedis.publish(channel, objectMapper.writeValueAsString(request))
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

    fun close() {
        jedis.close()
    }
}
