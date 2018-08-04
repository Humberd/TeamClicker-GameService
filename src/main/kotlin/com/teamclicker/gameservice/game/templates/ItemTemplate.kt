package com.teamclicker.gameservice.game.templates

class ItemTemplate {
    var id: Long = 0

    lateinit var name: String

    var atk: Int = 0
    var def: Int = 0
}

fun getItem(itemId: Long): ItemTemplate {
    return ItemTemplate().also {
        it.id = itemId
        it.name = "Mega Shield"
        it.atk = 5
        it.def = 10
    }
}