package com.teamclicker.gameservice.models.ws

data class AttackWTO(
    val playerId: Long,
    val waveId: String,
    val creatureId: String
)