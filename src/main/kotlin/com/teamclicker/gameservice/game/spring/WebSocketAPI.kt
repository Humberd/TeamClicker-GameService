package com.teamclicker.gameservice.game.spring

import com.teamclicker.gameservice.aop.WsSendTo
import org.springframework.stereotype.Service

@Service
class WebSocketAPI {

    @WsSendTo("/topic/greetings")
    fun gameState(): String {
        return "dupa"
    }

}