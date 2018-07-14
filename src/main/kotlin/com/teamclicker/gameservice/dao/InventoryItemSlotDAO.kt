package com.teamclicker.gameservice.dao

import javax.persistence.*

@Entity
@Table(name = "InventoryItemSlot")
class InventoryItemSlotDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "count", nullable = false)
    var count: Int = 1

    @Column(name = "itemId")
    var itemId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playerInventoryId", insertable=false, updatable=false)
    lateinit var playerInventory: PlayerInventoryDAO

    @Transient
    var item: ItemTemplate? = null

    @PrePersist
    fun saveItemId() {
        itemId = item?.id
    }

    @PostLoad
    fun loadItemObject() {
        itemId?.let {
            item = getItem(it)
        }
    }
}
