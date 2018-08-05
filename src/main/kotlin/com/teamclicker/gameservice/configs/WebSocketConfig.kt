package com.teamclicker.gameservice.configs

import ch.qos.logback.classic.Level
import com.teamclicker.gameservice.Constants
import com.teamclicker.gameservice.extensions.KLogging
import com.teamclicker.gameservice.mappers.ClaimsToJWTDataMapper
import com.teamclicker.gameservice.security.CryptoKeys
import com.teamclicker.gameservice.security.JWTAuthenticationToken
import io.jsonwebtoken.Jwts
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptorAdapter
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import java.lang.Exception


@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
class WebSocketConfig(
    private val claimsToJWTDataMapper: ClaimsToJWTDataMapper,
    private val cryptoKeys: CryptoKeys
) : AbstractSecurityWebSocketMessageBrokerConfigurer() {
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic")
        registry.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws")
            .setAllowedOrigins("*")
    }

    override fun sameOriginDisabled(): Boolean {
        return true
    }

    override fun customizeClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(object : ChannelInterceptorAdapter() {
            override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
                logger.info { "Pre Send" }
                val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)!!

                if (accessor.command === StompCommand.CONNECT) {
                    val header = accessor.getNativeHeader(Constants.JWT_HEADER_NAME)?.firstOrNull()

                    if (header === null || !header.startsWith(Constants.JWT_TOKEN_PREFIX)) {
                        logger.trace { "Request has no '${Constants.JWT_HEADER_NAME}' header or it doesn't start with '${Constants.JWT_TOKEN_PREFIX} '" }
                        return message
                    }

                    val jwtToken = header.replaceFirst(Constants.JWT_TOKEN_PREFIX, "")
                    val auth = getAuthentication(jwtToken)
                    accessor.user = auth
                    logger.info { "Saving token..." }
                }

                return message
            }
        })
    }

    private fun getAuthentication(jwtToken: String): Authentication {
        val jwtClaims = Jwts.parser()
            .setSigningKey(cryptoKeys.JWT_PUBLIC_KEY)
            .parseClaimsJws(jwtToken)
            .getBody()

        val jwtData = claimsToJWTDataMapper.parse(jwtClaims)

        return JWTAuthenticationToken(jwtData)
    }

    companion object : KLogging(Level.TRACE)
}