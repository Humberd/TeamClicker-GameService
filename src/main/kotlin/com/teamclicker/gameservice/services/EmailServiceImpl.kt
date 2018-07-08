package com.teamclicker.gameservice.services

import com.teamclicker.gameservice.kafka.KafkaSender
import com.teamclicker.gameservice.kafka.dto.PasswordResetEmailKDTO
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(
    private val kafkaSender: KafkaSender
) : EmailService {

    override fun sendPasswordResetEmail(email: String, token: String) {
        kafkaSender.send(PasswordResetEmailKDTO(email, token))
    }
}