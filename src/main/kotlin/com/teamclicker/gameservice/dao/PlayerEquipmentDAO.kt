package com.teamclicker.gameservice.dao

class PlayerEquipmentDAO {
    var id: Long = 0

    lateinit var headSlot: EquipmentItemSlot
    lateinit var bodySlot: EquipmentItemSlot
    lateinit var legsSlot: EquipmentItemSlot
    lateinit var bootsSlot: EquipmentItemSlot

    lateinit var hand1Slot: EquipmentItemSlot
    lateinit var hand2Slot: EquipmentItemSlot

    lateinit var neck1Slot: EquipmentItemSlot
    lateinit var nect2Slot: EquipmentItemSlot

    lateinit var finger1Slot: EquipmentItemSlot
    lateinit var finger2Slot: EquipmentItemSlot
    lateinit var finger3Slot: EquipmentItemSlot
    lateinit var finger4Slot: EquipmentItemSlot

    lateinit var usable1Slot: EquipmentItemSlot
    lateinit var usable2Slot: EquipmentItemSlot
    lateinit var usable3Slot: EquipmentItemSlot
    lateinit var usable4Slot: EquipmentItemSlot
}
