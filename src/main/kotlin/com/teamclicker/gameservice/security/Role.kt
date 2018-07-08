package com.teamclicker.gameservice.security

object Role {
    const val USER = "USER"
    const val ADMIN = "ADMIN"
}

enum class RoleType {
    USER,
    ADMIN
}

object SpELRole {
    const val _USER = "'USER'"
    const val _ADMIN = "'ADMIN'"
}