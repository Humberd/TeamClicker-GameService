package com.teamclicker.gameservice.security

import ch.qos.logback.classic.Level
import com.teamclicker.gameservice.extensions.KLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Service
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class JWTAuthorizationFilter(
    authManager: AuthenticationManager,
    private val authorizationHeaderExtractor: AuthorizationHeaderExtractor
) : BasicAuthenticationFilter(authManager) {
    override fun doFilterInternal(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain
    ) {
        val optionalAuth = authorizationHeaderExtractor.extractCredentials(req)
        if (optionalAuth.isPresent) {
            SecurityContextHolder.getContext().authentication = optionalAuth.get()
        }
        chain.doFilter(req, res)
    }

    companion object : KLogging(Level.TRACE)
}