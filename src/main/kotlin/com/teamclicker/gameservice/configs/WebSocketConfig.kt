package com.teamclicker.gameservice.configs

import ch.qos.logback.classic.Level
import com.teamclicker.gameservice.extensions.KLogging
import com.teamclicker.gameservice.security.JWTAuthorizationChannelInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry


@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
class WebSocketConfig(
    private val jwtAuthorizationChannelInterceptor: JWTAuthorizationChannelInterceptor
) : AbstractSecurityWebSocketMessageBrokerConfigurer() {
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic")
        registry.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws")
            .setAllowedOrigins("*")
            .withSockJS()
        registry.addEndpoint("/ws")
            .setAllowedOrigins("*")
    }

    override fun configureInbound(messages: MessageSecurityMetadataSourceRegistry) {
//        messages
//            .simpDestMatchers("/topic/greetings").authenticated()
    }

    override fun sameOriginDisabled(): Boolean {
        return true
    }

    override fun customizeClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(jwtAuthorizationChannelInterceptor)
    }

    companion object : KLogging(Level.TRACE)
}