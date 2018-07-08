package com.teamclicker.gameservice.testConfig.models

import com.teamclicker.gameservice.dao.EmailPasswordAuthDAO
import com.teamclicker.gameservice.dto.EPSignInDTO
import com.teamclicker.gameservice.dto.EPSignUpDTO

data class UserAccountMock(
    var email: String?,
    var password: String?
) {
    fun toEmailPasswordSignUp(): EPSignUpDTO {
        return EPSignUpDTO().also {
            it.email = email
            it.password = password
        }
    }

    fun toEmailPasswordSignIn(): EPSignInDTO {
        return EPSignInDTO().also {
            it.email = email
            it.password = password
        }
    }

    fun toEmailPasswordAuthDAO(): EmailPasswordAuthDAO {
        return EmailPasswordAuthDAO().also {
            it.email = email
            it.password = password
        }
    }
}