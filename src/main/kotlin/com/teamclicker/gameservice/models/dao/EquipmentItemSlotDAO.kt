package com.teamclicker.gameservice.models.dao

import com.teamclicker.gameservice.game.TemplatesResolver
import com.teamclicker.gameservice.game.templates.ItemTemplate
import mu.KLogging
import javax.persistence.*

@Entity
@Table(name = "EquipmentItemSlot")
class EquipmentItemSlotDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "isEnabled", nullable = false)
    var isEnabled: Boolean = false

    @Column(name = "itemId")
    var itemId: Long? = null

    @Transient
    var item: ItemTemplate? = null

    @PrePersist
    fun saveItemId() {
        itemId = item?.templateId
    }

    @PostLoad
    fun loadItemObject() {
        itemId?.let {
            item = TemplatesResolver.instance.getItemTemplate(it)
        }
    }

    constructor()

    constructor(isEnabled: Boolean) {
        this.isEnabled = isEnabled
    }

    companion object : KLogging()
}
