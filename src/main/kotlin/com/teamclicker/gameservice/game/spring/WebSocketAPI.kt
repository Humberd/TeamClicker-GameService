package com.teamclicker.gameservice.game.spring

import com.teamclicker.gameservice.aop.Param
import com.teamclicker.gameservice.aop.Payload
import com.teamclicker.gameservice.aop.WsSendTo
import com.teamclicker.gameservice.controllers.ws.Foo
import org.springframework.stereotype.Service

@Service
class WebSocketAPI {

    @WsSendTo("/topic/greetings/{name}")
    fun gameState(@Param name: String, @Payload body: Foo) {
    }
}