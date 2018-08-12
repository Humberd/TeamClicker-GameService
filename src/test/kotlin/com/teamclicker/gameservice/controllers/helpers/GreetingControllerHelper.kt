package com.teamclicker.gameservice.controllers.helpers

import com.teamclicker.gameservice.controllers.ws.Foo
import com.teamclicker.gameservice.testConfig.endpointBuilders.WebSocketSenderBuilder
import com.teamclicker.gameservice.testConfig.endpointBuilders.WebSocketSubscriptionBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.stereotype.Service

@Service
class GreetingControllerHelper {
    private var port: Int = 0

    fun sub() = Sub()
    fun send() = Send()

    inner class Sub :
        WebSocketSubscriptionBuilder<Sub, Foo>(Foo::class.java, port) {
        override val path = "/topic/greetings"
    }

    inner class Send :
        WebSocketSenderBuilder<Send, Foo>(port) {
        override val path = "/app/hello"
    }
}