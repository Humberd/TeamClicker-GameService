package com.teamclicker.gameservice.controllers.ws

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.teamclicker.gameservice.controllers.helpers.Users.ALICE
import com.teamclicker.gameservice.repositories.PlayerRepository
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.Transport
import org.springframework.web.socket.sockjs.client.WebSocketTransport
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GreetingControllerTest {
    @Value("\${local.server.port}")
    private val port: Int = 0
    @Autowired
    lateinit var playerRepository: PlayerRepository

    @Nested
    inner class Greetings {
        @Test
        fun `should test`() {
            val url = "ws://localhost:${port}/ws"
            println(url)
            val stompClient = WebSocketStompClient(SockJsClient(createTransportClient()))
            stompClient.messageConverter = MappingJackson2MessageConverter().also {
                it.objectMapper.registerModule(KotlinModule())
            }
            val stompSession =
                stompClient.connect(
                    url,
//                    WebSocketHttpHeaders().also { it.set("Authorization", "Bearer ${ALICE.token}")},
                    object : StompSessionHandlerAdapter() {}).get(1, TimeUnit.SECONDS)

            val completableFuture = CompletableFuture<Foo>()

            stompSession.subscribe("/topic/greetings", object : StompFrameHandler {
                override fun handleFrame(headers: StompHeaders, payload: Any?) {
                    println("New event: $payload")
//                    completableFuture.complete(payload as Foo)
                }

                override fun getPayloadType(headers: StompHeaders): Type {
                    return Foo::class.java
                }
            })

//            stompSession.send("/app/hello", Foo("Alice"))

            val state = completableFuture.get(1000000, TimeUnit.SECONDS)
            assertNotNull(state)
        }
    }

    private fun createTransportClient(): List<Transport> {
        val transports = ArrayList<Transport>(1)
        transports.add(WebSocketTransport(StandardWebSocketClient()))
        return transports
    }
}