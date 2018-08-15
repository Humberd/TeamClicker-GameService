package com.teamclicker.gameservice.models.dao

import com.teamclicker.gameservice.models.dto.PlayerDTO
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "Player")
class PlayerDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "accountId", nullable = false, unique = true, updatable = false)
    var accountId: Long = 0

    @Column(name = "name", nullable = false)
    lateinit var name: String
    @Column(name = "nameLc", nullable = false, unique = true)
    lateinit var nameLc: String

    @field:CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    lateinit var createdAt: LocalDateTime

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: PlayerStatus = PlayerStatus.OFFLINE

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "statsId", nullable = false)
    lateinit var stats: PlayerStatsDAO
    /* What palyer is currently wearing */
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "equipmentId", nullable = false)
    lateinit var equipment: PlayerEquipmentDAO
    /* Player's backpack */
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "inventoryId", nullable = false)
    lateinit var inventory: PlayerInventoryDAO

    @OneToMany(
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        mappedBy = "owner"
    )
    var friendList: List<FriendshipDAO> = arrayListOf()
    /* Friend requests the player had received */
    @OneToMany(
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        mappedBy = "receiver"
    )
    var friendRequestList: List<FriendRequestDAO> = arrayListOf()
    /* Friend requests the player had sent */
    @OneToMany(
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        mappedBy = "sender"
    )
    var friendRequestSentList: List<FriendRequestDAO> = arrayListOf()

    @PrePersist
    fun prePersist() = lowerCaseFields()

    @PreUpdate
    fun preUpdate() = lowerCaseFields()

    fun lowerCaseFields() {
        nameLc = name.toLowerCase()
    }

    fun toDTO(): PlayerDTO {
        return PlayerDTO(id, accountId, name, createdAt, status)
    }

}