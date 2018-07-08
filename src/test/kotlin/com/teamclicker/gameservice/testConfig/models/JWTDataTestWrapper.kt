package com.teamclicker.gameservice.testConfig.models

import com.teamclicker.gameservice.security.AuthenticationMethod

data class JWTDataTestWrapper(
    val accountId: Long,
    val authenticationMethod: AuthenticationMethod,
    val roles: Set<String>,
    val token: String
)

