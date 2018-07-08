package com.teamclicker.gameservice.kafka.dto

data class PasswordResetEmailKDTO(
    val email: String = "",
    val token: String = ""
)