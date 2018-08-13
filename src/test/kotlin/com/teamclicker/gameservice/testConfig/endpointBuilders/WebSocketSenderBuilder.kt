package com.teamclicker.gameservice.testConfig.endpointBuilders

interface Sender<in Body: Any> {
    fun send(body: Body)
}

abstract class WebSocketSenderBuilder<
        out Child : WebSocketSenderBuilder<Child, Body>,
        in Body : Any
        >(
    port: Int
) : WebSocketConnector<Child>(port), Sender<Body> {
    fun build(): Sender<Body> = this

    override fun send(body: Body) {
        ensureSession().send(resolvePath(), body)
    }
}
