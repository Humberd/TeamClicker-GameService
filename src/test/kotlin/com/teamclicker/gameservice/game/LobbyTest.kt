package com.teamclicker.gameservice.game

import com.teamclicker.gameservice.game.lobby.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class LobbyTest {

    @Nested
    inner class Invite {
        lateinit var lobby: Lobby

        @BeforeEach
        fun setUp() {
            lobby = Lobby(
                LobbySettings(
                    1,
                    LobbyStatus.PUBLIC
                )
            )
        }

        @Test
        fun `should not succeed when requester is not in the joined list`() {
            assertThrows(LobbyException::class.java) {
                lobby.invite(
                    123,
                    LobbyPlayer(
                        4,
                        LobbyPlayerStatus.MEMBER,
                        "",
                        5
                    )
                )
            }
        }

        @Test
        fun `should not succeed when requester has not rights to invite`() {
            lobby.joinedPlayersMap.put(123,
                LobbyPlayer(
                    123,
                    LobbyPlayerStatus.MEMBER,
                    "",
                    1
                )
            )
            assertThrows(LobbyException::class.java) {
                lobby.invite(
                    123,
                    LobbyPlayer(
                        4,
                        LobbyPlayerStatus.MEMBER,
                        "",
                        5
                    )
                )
            }
        }

        @Test
        fun `should not succeed when requester is in the joined list, but invited player is HOST`() {
            lobby.joinedPlayersMap.put(123,
                LobbyPlayer(
                    123,
                    LobbyPlayerStatus.HOST,
                    "",
                    1
                )
            )
            assertThrows(LobbyException::class.java) {
                lobby.invite(
                    123,
                    LobbyPlayer(
                        4,
                        LobbyPlayerStatus.HOST,
                        "",
                        5
                    )
                )
            }
        }

        @Test
        fun `should succeed`() {
            lobby.joinedPlayersMap.put(123,
                LobbyPlayer(
                    123,
                    LobbyPlayerStatus.HOST,
                    "",
                    1
                )
            )
            val invitedPlayer = LobbyPlayer(
                4,
                LobbyPlayerStatus.MEMBER,
                "",
                5
            )
            lobby.invite(
                123,
                invitedPlayer
            )

            assertEquals(lobby.invitedPlayersMap[4], invitedPlayer)
        }
    }

    @Nested
    inner class RemoveFromInvites {
        lateinit var lobby: Lobby

        @BeforeEach
        fun setUp() {
            lobby = Lobby(
                LobbySettings(
                    1,
                    LobbyStatus.PUBLIC
                )
            )
        }

        @Test
        fun `should not succeed when requester is not in the joined list`() {
            assertThrows(LobbyException::class.java) {
                lobby.removeFromInvites(123, 4)
            }
        }

        @Test
        fun `should not succeed when requester has not rights to invite`() {
            lobby.joinedPlayersMap.put(123,
                LobbyPlayer(
                    123,
                    LobbyPlayerStatus.MEMBER,
                    "",
                    1
                )
            )
            assertThrows(LobbyException::class.java) {
                lobby.removeFromInvites(123, 4)
            }
        }

        @Test
        fun `should succeed`() {
            lobby.joinedPlayersMap.put(123,
                LobbyPlayer(
                    123,
                    LobbyPlayerStatus.HOST,
                    "",
                    1
                )
            )
            lobby.invitedPlayersMap.put(4,
                LobbyPlayer(
                    4,
                    LobbyPlayerStatus.MEMBER,
                    "",
                    23
                )
            )

            lobby.removeFromInvites(123, 4)

            assertEquals(null, lobby.invitedPlayersMap[4])
        }
    }

    @Nested
    inner class FindInJoined {
        lateinit var lobby: Lobby

        @BeforeEach
        fun setUp() {
            lobby = Lobby(
                LobbySettings(
                    1,
                    LobbyStatus.PUBLIC
                )
            )
        }

        @Test
        fun `should throw exception when there is no searched player`() {
            lobby.joinedPlayersMap.put(123,
                LobbyPlayer(
                    123,
                    LobbyPlayerStatus.HOST,
                    "",
                    1
                )
            )

            assertThrows(LobbyException::class.java) {
                lobby.findInJoined(4423)
            }
        }

        @Test
        fun `should return player`() {
            val player = LobbyPlayer(
                123,
                LobbyPlayerStatus.HOST,
                "",
                1
            )
            lobby.joinedPlayersMap.put(123, player)

            assertEquals(player, lobby.findInJoined(123))
        }
    }

    @Nested
    inner class JoinSelf {
        lateinit var lobby: Lobby

        @Test
        fun `should not succeed when Lobby is PRIVATE, player is member and not in the invited list`() {
            lobby = Lobby(
                LobbySettings(
                    1,
                    LobbyStatus.PRIVATE
                )
            )
            val player = LobbyPlayer(
                4,
                LobbyPlayerStatus.MEMBER,
                "",
                43
            )

            assertThrows(LobbyException::class.java) {
                lobby.joinSelf(player)
            }
        }

        @Test
        fun `should succeed when lobby is PRIVATE, but player is HOST`() {
            lobby = Lobby(
                LobbySettings(
                    1,
                    LobbyStatus.PRIVATE
                )
            )
            val player = LobbyPlayer(
                4,
                LobbyPlayerStatus.HOST,
                "",
                43
            )

            lobby.joinSelf(player)

            assertEquals(null, lobby.invitedPlayersMap[4])
            assertEquals(player, lobby.joinedPlayersMap[4])
        }

        @Test
        fun `should succeed when lobby is PRIVATE, but player was invited`() {
            lobby = Lobby(
                LobbySettings(
                    1,
                    LobbyStatus.PRIVATE
                )
            )
            val player = LobbyPlayer(
                4,
                LobbyPlayerStatus.MEMBER,
                "",
                43
            )
            lobby.invitedPlayersMap.put(4, player)

            lobby.joinSelf(player)

            assertEquals(null, lobby.invitedPlayersMap[4])
            assertEquals(player, lobby.joinedPlayersMap[4])
        }

        @Test
        fun `should succeed when lobby is PUBLIC and player was not invited`() {
            lobby = Lobby(
                LobbySettings(
                    1,
                    LobbyStatus.PUBLIC
                )
            )
            val player = LobbyPlayer(
                4,
                LobbyPlayerStatus.MEMBER,
                "",
                43
            )

            lobby.joinSelf(player)

            assertEquals(null, lobby.invitedPlayersMap[4])
            assertEquals(player, lobby.joinedPlayersMap[4])
        }
    }

    @Nested
    inner class LeaveSelf {
        lateinit var lobby: Lobby

        @BeforeEach
        fun setUp() {
            lobby = Lobby(
                LobbySettings(
                    1,
                    LobbyStatus.PUBLIC
                )
            )
        }

        @Test
        fun `should not succeed when player does not exist in lobby`() {
            assertThrows(LobbyException::class.java) {
                lobby.leaveSelf(123)
            }
        }

        @Test
        fun `should succeed when player is MEMBER`() {
            lobby.joinedPlayersMap.put(123,
                LobbyPlayer(
                    123,
                    LobbyPlayerStatus.HOST,
                    "",
                    1
                )
            )
            lobby.joinedPlayersMap.put(77,
                LobbyPlayer(
                    77,
                    LobbyPlayerStatus.MEMBER,
                    "",
                    1
                )
            )

            lobby.leaveSelf(77)

            assertEquals(null, lobby.joinedPlayersMap[77])
        }

        @Test
        fun `should succeed and make another player a host when player is HOST`() {
            lobby.joinedPlayersMap.put(123,
                LobbyPlayer(
                    123,
                    LobbyPlayerStatus.HOST,
                    "",
                    1
                )
            )
            lobby.joinedPlayersMap.put(77,
                LobbyPlayer(
                    77,
                    LobbyPlayerStatus.MEMBER,
                    "",
                    1
                )
            )

            lobby.leaveSelf(123)

            assertEquals(null, lobby.joinedPlayersMap[123])
            assertEquals(
                LobbyPlayer(
                    77,
                    LobbyPlayerStatus.HOST,
                    "",
                    1
                ), lobby.joinedPlayersMap[77])
        }

        @Test
        fun `should succeed when player is HOST and there is no other player to pass HOST onto`() {
            lobby.joinedPlayersMap.put(123,
                LobbyPlayer(
                    123,
                    LobbyPlayerStatus.HOST,
                    "",
                    1
                )
            )

            lobby.leaveSelf(123)

            assertEquals(null, lobby.joinedPlayersMap[123])
            assertEquals(0, lobby.joinedPlayersMap.size)
        }
    }

    @Nested
    inner class Kick {
        lateinit var lobby: Lobby

        @BeforeEach
        fun setUp() {
            lobby = Lobby(
                LobbySettings(
                    1,
                    LobbyStatus.PUBLIC
                )
            )
        }

        @Test
        fun `should not succeed when requester is not in the joined list`() {
            lobby.joinedPlayersMap.put(4,
                LobbyPlayer(
                    4,
                    LobbyPlayerStatus.MEMBER,
                    "",
                    1
                )
            )
            assertThrows(LobbyException::class.java) {
                lobby.kick(123, 4)
            }
        }

        @Test
        fun `should not succeed when requester is MEMBER`() {
            lobby.joinedPlayersMap.put(123,
                LobbyPlayer(
                    4,
                    LobbyPlayerStatus.MEMBER,
                    "",
                    1
                )
            )
            lobby.joinedPlayersMap.put(4,
                LobbyPlayer(
                    4,
                    LobbyPlayerStatus.MEMBER,
                    "",
                    1
                )
            )
            assertThrows(LobbyException::class.java) {
                lobby.kick(123, 4)
            }
        }

        @Test
        fun `should succeed`() {
            lobby.joinedPlayersMap.put(123,
                LobbyPlayer(
                    4,
                    LobbyPlayerStatus.HOST,
                    "",
                    1
                )
            )
            lobby.joinedPlayersMap.put(4,
                LobbyPlayer(
                    4,
                    LobbyPlayerStatus.MEMBER,
                    "",
                    1
                )
            )

            lobby.kick(123, 4)

            assertEquals(null, lobby.joinedPlayersMap[4])
        }
    }
}