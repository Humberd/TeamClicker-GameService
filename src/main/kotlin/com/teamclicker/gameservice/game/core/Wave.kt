package com.teamclicker.gameservice.game.core

import com.teamclicker.gameservice.game.templates.CreatureTemplate
import com.teamclicker.gameservice.game.templates.WaveTemplate
import com.teamclicker.gameservice.utils.Generators
import mu.KLogging

class Wave(
    val waveId: String,
    val template: WaveTemplate
) {
    val creatures: Map<String, GameCreature>

    init {
        val gameCreaturesList = template.creatures.map {
            val id = Generators.creatureId()
            Pair(
                id, GameCreature(
                    creatureId = id,
                    template = it
                )
            )
        }

        creatures = mapOf(*gameCreaturesList.toTypedArray())
    }

    fun getCreature(creatureId: String): GameCreature {
        creatures[creatureId]?.let { return it }
        throw GameException("Creature does not exist.")
    }

    companion object : KLogging()
}

class GameCreature(
    val creatureId: String,
    val template: CreatureTemplate
)