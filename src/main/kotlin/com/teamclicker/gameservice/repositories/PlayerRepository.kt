package com.teamclicker.gameservice.repositories

import com.teamclicker.gameservice.dao.PlayerDAO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerRepository: JpaRepository<PlayerDAO, Long> {

}