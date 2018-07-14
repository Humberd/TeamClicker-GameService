package com.teamclicker.gameservice.dao

import javax.persistence.*

@Entity
@Table(name = "PlayerStats")
class PlayerStatsDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "level", nullable = false)
    var level: Int = 0
    @Column(name = "experience", nullable = false)
    var experience: Long = 0

    @Column(name = "maxHp", nullable = false)
    var maxHp: Int = 0
    @Column(name = "hp", nullable = false)
    var hp: Int = 0

    @Column(name = "atk", nullable = false)
    var atk: Int = 0
    @Column(name = "def", nullable = false)
    var def: Int = 0

    @Column(name = "gold", nullable = false)
    var gold: Int = 0
    @Column(name = "diamonds", nullable = false)
    var diamonds: Int = 0
}
