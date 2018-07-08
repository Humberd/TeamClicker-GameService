package com.teamclicker.gameservice.services

interface HashingService {
    fun hashBySHA_256(data: String): String
}