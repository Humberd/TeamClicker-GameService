package com.teamclicker.gameservice.controllers.ws

import com.teamclicker.gameservice.extensions.KLogging
import com.teamclicker.gameservice.game.spring.WebSocketAPI
import com.teamclicker.gameservice.security.isAuthenticated
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller

@Controller
class GreetingController(
    val webSocketAPI: WebSocketAPI
) {

    @PreAuthorize(isAuthenticated)
    @MessageMapping("/hello")
    fun greeting(message: Foo) {
        Thread.sleep(1000) // simulated delay
        logger.info { "Greetings $message" }
        webSocketAPI.gameState()
    }

    companion object : KLogging()
}

data class Foo(
    val name: String
)