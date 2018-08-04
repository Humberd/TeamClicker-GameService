package com.teamclicker.gameservice.controllers.ws

import com.teamclicker.gameservice.extensions.KLogging
import com.teamclicker.gameservice.game.spring.WebSocketAPI
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class GreetingController(
    val webSocketAPI: WebSocketAPI
) {

    //    @PreAuthorize("permitAll()")
    @MessageMapping("/hello")
    fun greeting(message: Foo) {
        Thread.sleep(1000) // simulated delay
        logger.info { "Greetings $message" }
        webSocketAPI.gameState()
    }

    companion object : KLogging()
}

data class Foo(
    var name: String
)