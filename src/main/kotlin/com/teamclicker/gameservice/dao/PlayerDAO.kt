package com.teamclicker.gameservice.dao

import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "Player")
class PlayerDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "accountId", nullable = false)
    var accountId: Long = 0

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    lateinit var createdAt: Date

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

    @OneToMany(cascade = [CascadeType.ALL],orphanRemoval = true, mappedBy = "owner")
    var friendList: List<FriendshipDAO> = arrayListOf()
//    /* Friend requests the player had received */
//    var friendRequestList: ArrayList<FriendRequestDAO> = arrayListOf()
//    /* Friend requests the player had sent */
//    var friendRequestSentList: ArrayList<FriendRequestDAO> = arrayListOf()
}