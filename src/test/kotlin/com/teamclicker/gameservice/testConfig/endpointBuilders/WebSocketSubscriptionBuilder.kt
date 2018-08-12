package com.teamclicker.gameservice.testConfig.endpointBuilders

import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import java.lang.reflect.Type

@Suppress("UNCHECKED_CAST")
abstract class WebSocketSubscriptionBuilder<
        out Child : WebSocketSubscriptionBuilder<Child, Response>,
        Response : Any
        >(
    private val responseType: Type,
    port: Int
) : WebSocketConnector<Child>(port) {
    fun subscribe(callback: (Response) -> Unit = {}): StompSession.Subscription {
        return ensureSession()
            .subscribe(resolvePath(), StompFrameHandlerImpl(callback))
    }

    inner class StompFrameHandlerImpl(
        val callback: (Response) -> Unit
    ) : StompFrameHandler {
        override fun handleFrame(headers: StompHeaders, payload: Any?) {
            callback(payload as Response)
        }

        override fun getPayloadType(headers: StompHeaders) = responseType
    }
}