package com.teamclicker.gameservice.models.dao

import com.teamclicker.gameservice.game.spring.TemplatesStore
import com.teamclicker.gameservice.game.templates.ItemTemplate
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
    @JoinColumn(name = "playerInventoryId", insertable = false, updatable = false)
    lateinit var playerInventory: PlayerInventoryDAO

    @Transient
    var item: ItemTemplate? = null

    @PrePersist
    fun saveItemId() {
        itemId = item?.templateId
    }

    @PostLoad
    fun loadItemObject() {
        itemId?.let {
            item = TemplatesStore.instance.getItemTemplate(it)
        }
    }
}
