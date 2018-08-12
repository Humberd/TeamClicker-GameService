package com.teamclicker.gameservice.security

import ch.qos.logback.classic.Level
import com.teamclicker.gameservice.configs.WebSocketConfig
import com.teamclicker.gameservice.extensions.KLogging
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptorAdapter
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Service

@Service
class JWTAuthorizationChannelInterceptor(
    private val authorizationHeaderExtractor: AuthorizationHeaderExtractor
) : ChannelInterceptorAdapter() {
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)!!

        if (accessor.command === StompCommand.CONNECT) {
            val optionalAuth = authorizationHeaderExtractor.extractCredentials(accessor)
            if (optionalAuth.isPresent) {
                accessor.user = optionalAuth.get()
                logger.info { "Saving token..." }
            }
        }

        return message
    }


    companion object : KLogging(Level.TRACE)
}