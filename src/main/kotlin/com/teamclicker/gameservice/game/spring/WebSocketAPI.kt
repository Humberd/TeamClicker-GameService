package com.teamclicker.gameservice.game.spring

import com.teamclicker.gameservice.aop.WsSendTo
import com.teamclicker.gameservice.controllers.ws.Foo
import com.teamclicker.gameservice.security.isAuthenticated
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class WebSocketAPI {

    @PreAuthorize(isAuthenticated)
    @WsSendTo("/topic/greetings")
    fun gameState(): Foo {
        return Foo("bar")
    }
}