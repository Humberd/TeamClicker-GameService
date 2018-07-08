package com.teamclicker.gameservice.repositories

import com.teamclicker.gameservice.dao.UserRoleDAO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRoleRepository : JpaRepository<UserRoleDAO, String>