package com.teamclicker.gameservice.utils

import java.util.*

object Generators {
    fun randomStringId() = UUID.randomUUID().toString()

    fun gameId() = "game_${randomStringId()}"

    fun waveId() = "wave_${randomStringId()}"

    fun creatureId() = "creature_${randomStringId()}"
}