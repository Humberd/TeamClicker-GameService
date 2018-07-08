package com.teamclicker.gameservice.testConfig.helpers

import com.teamclicker.gameservice.Constants.JWT_HEADER_NAME
import com.teamclicker.gameservice.Constants.JWT_TOKEN_PREFIX
import com.teamclicker.gameservice.mappers.ClaimsToJWTDataMapper
import com.teamclicker.gameservice.security.CryptoKeys
import com.teamclicker.gameservice.testConfig.models.JWTDataTestWrapper
import io.jsonwebtoken.Jwts
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class JwtExtractorHelper(
    private val claimsToJWTDataMapper: ClaimsToJWTDataMapper,
    private val cryptoKeys: CryptoKeys
) {
    fun getJwtData(response: ResponseEntity<*>): JWTDataTestWrapper {
        val token = response.headers.get(JWT_HEADER_NAME)?.get(0)
        val rawToken = token?.replaceFirst(JWT_TOKEN_PREFIX, "")
        val jwtClaims = Jwts.parser()
            .setSigningKey(cryptoKeys.JWT_PUBLIC_KEY)
            .parseClaimsJws(rawToken)
            .getBody()

        val jwtData = claimsToJWTDataMapper.parse(jwtClaims)
        return JWTDataTestWrapper(
            jwtData.accountId,
            jwtData.authenticationMethod,
            jwtData.roles,
            token!!
        )
    }
}