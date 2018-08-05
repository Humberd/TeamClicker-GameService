package com.teamclicker.gameservice.security

import com.teamclicker.gameservice.Constants.JWT_HEADER_NAME
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurity(
    @Lazy private val jwtAuthorizationFilter: JWTAuthorizationFilter
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.cors().disable()
            .csrf().disable()
            .httpBasic().disable()
            .formLogin().disable()
//            .authorizeRequests().antMatchers("/ws").authenticated()
//            .and()
            .addFilter(jwtAuthorizationFilter)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfig = CorsConfiguration()
            .applyPermitDefaultValues().apply {
                addAllowedHeader(JWT_HEADER_NAME)
                addExposedHeader(JWT_HEADER_NAME)
                addAllowedMethod(HttpMethod.GET)
                addAllowedMethod(HttpMethod.POST)
                addAllowedMethod(HttpMethod.PUT)
                addAllowedMethod(HttpMethod.DELETE)
                addAllowedMethod(HttpMethod.HEAD)
                addAllowedMethod(HttpMethod.OPTIONS)
            }

        source.registerCorsConfiguration("/**", corsConfig)
        return source
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}
