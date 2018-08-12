package com.teamclicker.gameservice.testConfig.endpointBuilders

abstract class WebSocketSenderBuilder<
        out Child: WebSocketSenderBuilder<Child, Body>,
        Body: Any
        >(
    port: Int
): WebSocketConnector<Child>(port) {
    fun send(body: Body) {
        ensureSession().send(resolvePath(), body)
    }
}