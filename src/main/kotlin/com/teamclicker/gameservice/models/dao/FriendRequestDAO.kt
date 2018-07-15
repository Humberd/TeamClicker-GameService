package com.teamclicker.gameservice.models.dao

import org.hibernate.annotations.CreationTimestamp
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "FriendRequest")
class FriendRequestDAO : Serializable{
    @field:CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    lateinit var createdAt: LocalDateTime

    @Column(name = "completedAt")
    var completedAt: LocalDateTime? = null

    @Id
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "senderId", nullable = false)
    lateinit var sender: PlayerDAO

    @Id
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "receiverId", nullable = false)
    lateinit var receiver: PlayerDAO

    fun isCompleted() = completedAt !== null
}
