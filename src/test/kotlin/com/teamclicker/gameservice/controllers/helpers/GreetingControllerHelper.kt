package com.teamclicker.gameservice.controllers.helpers

import com.teamclicker.gameservice.controllers.ws.Foo
import com.teamclicker.gameservice.testConfig.endpointBuilders.WebSocketSenderBuilder
import com.teamclicker.gameservice.testConfig.endpointBuilders.WebSocketSubscriptionBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
@Lazy
class GreetingControllerHelper {
    @Value("\${local.server.port}")
    private var port: Int = 0

    fun sub() = Sub()
    fun send() = Send()

    inner class Sub :
        WebSocketSubscriptionBuilder<Sub, Foo>(Foo::class.java, port) {
        override val path = "/topic/greetings/{name}"

        fun name(name: String) = addPathVariable("name", name)
    }

    inner class Send :
        WebSocketSenderBuilder<Send, Foo>(port) {
        override val path = "/app/hello"
    }
}