package com.teamclicker.gameservice.security

import ch.qos.logback.classic.Level
import com.teamclicker.gameservice.Constants
import com.teamclicker.gameservice.extensions.KLogging
import com.teamclicker.gameservice.mappers.ClaimsToJWTDataMapper
import io.jsonwebtoken.Jwts
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpServletRequest

@Service
class AuthorizationHeaderExtractor(
    private val claimsToJWTDataMapper: ClaimsToJWTDataMapper,
    private val cryptoKeys: CryptoKeys
) {
    fun extractCredentials(req: HttpServletRequest): Optional<Authentication> {
        val header = req.getHeader(Constants.JWT_HEADER_NAME)

        return headerParser(header)
    }

    fun extractCredentials(accessor: StompHeaderAccessor): Optional<Authentication> {
        val header = accessor.getNativeHeader(Constants.JWT_HEADER_NAME)?.firstOrNull()

        return headerParser(header)
    }

    internal fun headerParser(header: String?): Optional<Authentication> {
        if (header === null || !header.startsWith(Constants.JWT_TOKEN_PREFIX)) {
            logger.trace { "Request has no '${Constants.JWT_HEADER_NAME}' header or it doesn't start with '${Constants.JWT_TOKEN_PREFIX} '" }
            return Optional.empty()
        }

        val jwtToken = header.replaceFirst(Constants.JWT_TOKEN_PREFIX, "")

        return Optional.of(getAuthentication(jwtToken))
    }

    internal fun getAuthentication(jwtToken: String): Authentication {
        val jwtClaims = Jwts.parser()
            .setSigningKey(cryptoKeys.JWT_PUBLIC_KEY)
            .parseClaimsJws(jwtToken)
            .getBody()

        val jwtData = claimsToJWTDataMapper.parse(jwtClaims)

        return JWTAuthenticationToken(jwtData)
    }

    companion object : KLogging(Level.TRACE)
}