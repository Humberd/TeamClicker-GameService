package com.teamclicker.gameservice.dao

import org.hibernate.annotations.CreationTimestamp
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "Friendship")
class FriendshipDAO : Serializable{
    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    lateinit var createdAt: Date

    @Id
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "ownerId", nullable = false)
    lateinit var owner: PlayerDAO

    @Id
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "friendId", nullable = false)
    lateinit var friend: PlayerDAO
}

//https://stackoverflow.com/questions/26626920/hibernate-how-to-joincolumn-an-embeddedid