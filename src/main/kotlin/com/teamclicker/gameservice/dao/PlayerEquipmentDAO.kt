package com.teamclicker.gameservice.dao

class PlayerEquipmentDAO {
    var id: Long = 0

    lateinit var headSlot: EquipmentItemSlotDAO
    lateinit var bodySlot: EquipmentItemSlotDAO
    lateinit var legsSlot: EquipmentItemSlotDAO
    lateinit var bootsSlot: EquipmentItemSlotDAO

    lateinit var hand1Slot: EquipmentItemSlotDAO
    lateinit var hand2Slot: EquipmentItemSlotDAO

    lateinit var neck1Slot: EquipmentItemSlotDAO
    lateinit var nect2Slot: EquipmentItemSlotDAO

    lateinit var finger1Slot: EquipmentItemSlotDAO
    lateinit var finger2Slot: EquipmentItemSlotDAO
    lateinit var finger3Slot: EquipmentItemSlotDAO
    lateinit var finger4Slot: EquipmentItemSlotDAO

    lateinit var usable1Slot: EquipmentItemSlotDAO
    lateinit var usable2Slot: EquipmentItemSlotDAO
    lateinit var usable3Slot: EquipmentItemSlotDAO
    lateinit var usable4Slot: EquipmentItemSlotDAO
}
