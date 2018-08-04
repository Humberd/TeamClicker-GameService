package com.teamclicker.gameservice.repositories

import com.teamclicker.gameservice.models.dao.PlayerDAO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerRepository : JpaRepository<PlayerDAO, Long> {

}