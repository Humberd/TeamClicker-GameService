package com.teamclicker.gameservice.game.spring

import com.teamclicker.gameservice.aop.WsSendTo
import com.teamclicker.gameservice.controllers.ws.Foo
import org.springframework.stereotype.Service

@Service
class WebSocketAPI {

    @WsSendTo("/topic/greetings")
    fun gameState(): Foo {
        return Foo("bar")
    }
}