package com.teamclicker.gameservice.services

interface EmailService {
    fun sendPasswordResetEmail(email: String, token: String)
}