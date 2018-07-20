package com.teamclicker.gameservice.game.core

import com.teamclicker.gameservice.models.templates.CreatureTemplate
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import mu.KLogging

class Wave(
    val waveId: String,
    val creatures: Map<String, CreatureTemplate>
) {

    internal fun getCreature(creatureId: String): CreatureTemplate {
        creatures[creatureId]?.let { return it }
        throw GameException("Creature does not exist.")
    }

    companion object: KLogging()
}