package com.teamclicker.gameservice.repositories

import com.teamclicker.gameservice.models.dao.PlayerDAO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PlayerRepository : JpaRepository<PlayerDAO, Long> {
    @Query(
        """
        select case when (count (player) > 0) then true else false end
        from PlayerDAO as player
        where player.nameLc = :nameLc
    """
    )
    fun existsByName(@Param("nameLc") nameLc: String): Boolean

    @Query(
        """
        select case when (count (player) > 0) then true else false end
        from PlayerDAO as player
        where player.id = :playerId
        and player.accountId = :accountId
    """
    )
    fun existsBy(
        @Param("playerId") playerId: Long,
        @Param("accountId") accountId: Long
    ): Boolean

    @Query(
        """
        select player
        from PlayerDAO as player
        where player.id = :playerId
        and player.accountId = :accountId
    """
    )
    fun findBy(
        @Param("playerId") playerId: Long,
        @Param("accountId") accountId: Long
    ): Optional<PlayerDAO>

    @Query(
        """
        select player
        from PlayerDAO as player
        where player.nameLc = :nameLc
    """
    )
    fun findByName(@Param("nameLc") nameLc: String): Optional<PlayerDAO>
}
