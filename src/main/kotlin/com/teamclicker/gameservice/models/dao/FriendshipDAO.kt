package com.teamclicker.gameservice.models.dao

import org.hibernate.annotations.CreationTimestamp
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "Friendship")
class FriendshipDAO : Serializable {
    @field:CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    lateinit var createdAt: LocalDateTime

    @Id
    @ManyToOne()
    @JoinColumn(name = "ownerId", nullable = false)
    lateinit var owner: PlayerDAO

    @Id
    @ManyToOne()
    @JoinColumn(name = "friendId", nullable = false)
    lateinit var friend: PlayerDAO
}
