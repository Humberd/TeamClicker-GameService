package com.teamclicker.gameservice.testConfig.helpers

import com.teamclicker.gameservice.dao.UserAccountDAO
import com.teamclicker.gameservice.dao.UserAccountDeletionDAO
import com.teamclicker.gameservice.repositories.UserAccountRepository
import com.teamclicker.gameservice.testConfig.models.UserAccountMock

class UserAccountRepositoryHelper(private val userAccountRepository: UserAccountRepository) {
    fun add(userAccount: UserAccountMock): UserAccountDAO {
        return userAccountRepository.save(UserAccountDAO().also {
            it.emailPasswordAuth = userAccount.toEmailPasswordAuthDAO()
        })
    }

    fun delete(userAccountDAO: UserAccountDAO) {
        userAccountDAO.deletion = UserAccountDeletionDAO()
        userAccountRepository.save(userAccountDAO)
    }
}