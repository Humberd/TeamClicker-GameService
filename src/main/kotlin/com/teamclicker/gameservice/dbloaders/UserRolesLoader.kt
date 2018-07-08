package com.teamclicker.gameservice.dbloaders

import com.teamclicker.gameservice.security.Role
import com.teamclicker.gameservice.dao.UserRoleDAO
import com.teamclicker.gameservice.repositories.UserRoleRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class UserRolesLoader(
    private val userRoleRepository: UserRoleRepository
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        userRoleRepository.saveAll(
            listOf(
                UserRoleDAO(Role.USER),
                UserRoleDAO(Role.ADMIN)
            )
        )
    }
}