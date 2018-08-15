package com.teamclicker.gameservice.controllers.ws

import com.teamclicker.gameservice.extensions.KLogging
import com.teamclicker.gameservice.game.spring.WebSocketAPI
import com.teamclicker.gameservice.security.isAuthenticated
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import javax.annotation.PostConstruct
import kotlin.concurrent.timer
import kotlin.math.log

@Controller
class GreetingController(
    private val webSocketAPI: WebSocketAPI,
    private val simpleMessagingTemplate: SimpMessagingTemplate
) {

    @PostConstruct
    fun postConstruct() {
//        timer(period = 1000) {
//            logger.info { "Sending a message" }
//            simpleMessagingTemplate.convertAndSendToUser("62", "/topic/reply", "This is a message")
//        }
    }

    @PreAuthorize(isAuthenticated)
    @MessageMapping("/hello")
    fun greeting(message: Foo) {
        Thread.sleep(1000) // simulated delay
        logger.info { "Greetings $message" }
        webSocketAPI.gameState(message.name, message)
    }

    companion object : KLogging()
}

data class Foo(
    val name: String
)