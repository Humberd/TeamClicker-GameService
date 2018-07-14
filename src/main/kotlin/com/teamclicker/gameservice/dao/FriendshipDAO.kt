package com.teamclicker.gameservice.dao

import org.springframework.data.annotation.CreatedDate
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "Friendship")
class FriendshipDAO {
    @EmbeddedId
    var id = FriendshipId()

    @CreatedDate
    @Column(name = "createdAt")
    lateinit var createdAt: Date

    @ManyToOne()
    @MapsId("ownerId")
    lateinit var owner: PlayerDAO

    @ManyToOne
    @MapsId("friendId")
    lateinit var friend: PlayerDAO

    @Embeddable
    class FriendshipId : Serializable{
        var ownerId: Long = 0
        var friendId: Long = 0
    }
}

//https://stackoverflow.com/questions/26626920/hibernate-how-to-joincolumn-an-embeddedid