package com.teamclicker.gameservice.testConfig.endpointBuilders

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.teamclicker.gameservice.Constants
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.Transport
import org.springframework.web.socket.sockjs.client.WebSocketTransport
import java.util.*
import java.util.concurrent.TimeUnit.SECONDS

@Suppress("UNCHECKED_CAST")
abstract class WebSocketConnector<out Child : WebSocketConnector<Child>>(
    private val port: Int
) {
    protected val headers = WebSocketHttpHeaders()
    protected val pathVariables = hashMapOf<String, Any>()

    abstract val path: String

    protected var session = Optional.empty<StompSession>()

    /**
     * Sends user token in the header
     */
    fun with(user: TestEntity): Child {
        user.token?.let {
            headers[Constants.JWT_HEADER_NAME] = "${Constants.JWT_TOKEN_PREFIX}$it"
        }

        return this as Child
    }

    fun addPathVariable(key: String, value: Any): Child {
        this.pathVariables.put(key, value)
        return this as Child
    }

    fun addHeader(key: String, value: String): Child {
        this.headers.add(key, value)
        return this as Child
    }

    protected fun ensureSession(): StompSession {
        return session.orElseGet {
            connect().also {
                session = Optional.of(it)
            }
        }
    }

    protected fun connect(): StompSession {
        return createStompClient()
            .connect("ws://localhost:$port/ws", headers, object : StompSessionHandlerAdapter() {})
            .get(1, SECONDS)
    }

    protected fun resolvePath(): String {
        var tempUrl = path
        for ((key, value) in pathVariables) {
            tempUrl = tempUrl.replace("{$key}", value.toString())
        }
        return tempUrl
    }

    private fun createStompClient(): WebSocketStompClient {
        val stompClient = WebSocketStompClient(SockJsClient(createTransportClient()))
        stompClient.messageConverter = MappingJackson2MessageConverter().also {
            it.objectMapper.registerModule(KotlinModule())
        }

        return stompClient
    }

    private fun createTransportClient(): List<Transport> {
        val transports = ArrayList<Transport>(1)
        transports.add(WebSocketTransport(StandardWebSocketClient()))
        return transports
    }
}