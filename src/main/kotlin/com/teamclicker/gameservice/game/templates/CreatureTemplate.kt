package com.teamclicker.gameservice.game.templates

data class CreatureTemplate(
    val templateId: Long,

    var name: String,

    var maxHp: Int,
    var hp: Int,

    var atk: Int,
    var def: Int,

    var atkSpeed: Float,

    var drop: CreatureDropTemplate
)

data class CreatureDropTemplate(
    val experience: Long
)