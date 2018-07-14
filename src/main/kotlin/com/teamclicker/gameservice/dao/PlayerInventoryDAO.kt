package com.teamclicker.gameservice.dao

import javax.persistence.*

@Entity
@Table(name = "PlayerInventory")
class PlayerInventoryDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name= "playerInventoryId")
    var itemListDAO: List<InventoryItemSlotDAO> = arrayListOf()
}
