package com.teamclicker.gameservice.game.templates

class CreatureTemplate {
    var templateId: Long = 0

    var maxHp: Int = 0
    var hp: Int = 0

    var atk: Int = 0
    var def: Int = 0

    var atkSpeed: Float = 0f

    lateinit var drop: CreatureDropTemplate
}