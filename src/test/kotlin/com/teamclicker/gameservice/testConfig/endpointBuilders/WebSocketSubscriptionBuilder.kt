package com.teamclicker.gameservice.testConfig.endpointBuilders

import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import java.lang.reflect.Type
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit.SECONDS

@Suppress("UNCHECKED_CAST")
abstract class WebSocketSubscriptionBuilder<
        out Child : WebSocketSubscriptionBuilder<Child, Response>,
        Response : Any
        >(
    private val responseType: Type,
    port: Int
) : WebSocketConnector<Child>(port) {

    fun subscribe(callback: (Response) -> Unit = {}): Subscription<Response> {
        val responseQueue = LinkedBlockingDeque<Response>()

        val callbackWrapper: (Response) -> Unit = {
            responseQueue.add(it)
            callback(it)
        }
        val destination = resolvePath()
        val subscription =
            ensureSession()
                .subscribe(destination, StompFrameHandlerImpl(callbackWrapper))

        return Subscription(subscription, responseQueue, destination)
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

interface Unsubscribeable {
    fun unsubscribe()
}

class Subscription<Response>(
    private val subscription: StompSession.Subscription,
    private val responseQueue: LinkedBlockingDeque<Response>,
    private val destination: String
) : Unsubscribeable {

    override fun unsubscribe() {
        subscription.unsubscribe()
    }

    fun expectEvent(): Response {
        responseQueue.poll(1, SECONDS)?.let { return it }
        throw EventExpectedException("Expected event from $destination, but it did not arrive.")
    }

    fun expectNoEvent() {
        responseQueue.poll(1, SECONDS)?.let {
            throw EventNotExpectedException("Unexpected event from $destination, \nPayload: $it\n")
        }
    }
}