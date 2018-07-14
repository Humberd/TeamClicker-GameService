package com.teamclicker.gameservice.dao

import javax.persistence.*

@Entity
@Table(name = "PlayerEquipment")
class PlayerEquipmentDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "headSlotId", nullable = false)
    lateinit var headSlot: EquipmentItemSlotDAO
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "bodySlotId", nullable = false)
    lateinit var bodySlot: EquipmentItemSlotDAO
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "legsSlotId", nullable = false)
    lateinit var legsSlot: EquipmentItemSlotDAO
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "bootsSlotId", nullable = false)
    lateinit var bootsSlot: EquipmentItemSlotDAO

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "hand1SlotId", nullable = false)
    lateinit var hand1Slot: EquipmentItemSlotDAO
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "hand2SlotId", nullable = false)
    lateinit var hand2Slot: EquipmentItemSlotDAO

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "neck1SlotId", nullable = false)
    lateinit var neck1Slot: EquipmentItemSlotDAO
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "nect2SlotId", nullable = false)
    lateinit var nect2Slot: EquipmentItemSlotDAO

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "finger1SlotId", nullable = false)
    lateinit var finger1Slot: EquipmentItemSlotDAO
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "finger2SlotId", nullable = false)
    lateinit var finger2Slot: EquipmentItemSlotDAO
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "finger3SlotId", nullable = false)
    lateinit var finger3Slot: EquipmentItemSlotDAO
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "finger4SlotId", nullable = false)
    lateinit var finger4Slot: EquipmentItemSlotDAO

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "usable1SlotId", nullable = false)
    lateinit var usable1Slot: EquipmentItemSlotDAO
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "usable2SlotId", nullable = false)
    lateinit var usable2Slot: EquipmentItemSlotDAO
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "usable3SlotId", nullable = false)
    lateinit var usable3Slot: EquipmentItemSlotDAO
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "usable4SlotId", nullable = false)
    lateinit var usable4Slot: EquipmentItemSlotDAO

    companion object {
        fun create(): PlayerEquipmentDAO {
            return PlayerEquipmentDAO().apply {
                headSlot = EquipmentItemSlotDAO(true)
                bodySlot = EquipmentItemSlotDAO(true)
                legsSlot = EquipmentItemSlotDAO(true)
                bootsSlot = EquipmentItemSlotDAO(true)

                hand1Slot = EquipmentItemSlotDAO(true)
                hand2Slot = EquipmentItemSlotDAO(true)

                neck1Slot = EquipmentItemSlotDAO()
                nect2Slot = EquipmentItemSlotDAO()

                finger1Slot = EquipmentItemSlotDAO()
                finger2Slot = EquipmentItemSlotDAO()
                finger3Slot = EquipmentItemSlotDAO()
                finger4Slot = EquipmentItemSlotDAO()

                usable1Slot = EquipmentItemSlotDAO()
                usable2Slot = EquipmentItemSlotDAO()
                usable3Slot = EquipmentItemSlotDAO()
                usable4Slot = EquipmentItemSlotDAO()
            }
        }
    }
}
