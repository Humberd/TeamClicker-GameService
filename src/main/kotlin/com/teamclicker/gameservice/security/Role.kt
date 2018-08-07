package com.teamclicker.gameservice.security

import org.intellij.lang.annotations.Language

object Role {
    const val USER = "USER"
    const val ADMIN = "ADMIN"
}

enum class RoleType {
    USER,
    ADMIN
}


@Language("SpEL")
const val isAdmin = "hasAuthority('${Role.ADMIN}')"

@Language("SpEL")
const val isUser = "hasAuthority('${Role.USER}')"

@Language("SpEL")
const val isUserOrAdmin = "hasAnyAuthority('${Role.USER}', ${Role.ADMIN})"

@Language("SpEL")
const val isAuthenticated = "isAuthenticated()"