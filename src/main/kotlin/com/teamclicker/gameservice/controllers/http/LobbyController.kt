package com.teamclicker.gameservice.controllers.http

import com.teamclicker.gameservice.exceptions.EntityDoesNotExistException
import com.teamclicker.gameservice.extensions.KLogging
import com.teamclicker.gameservice.game.lobby.LobbyPlayer
import com.teamclicker.gameservice.game.spring.LobbyService
import com.teamclicker.gameservice.models.dto.*
import com.teamclicker.gameservice.repositories.PlayerRepository
import com.teamclicker.gameservice.security.JWTData
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.transaction.Transactional
import javax.validation.Valid

@CrossOrigin
@RestController
@RequestMapping("/api/lobbies")
class LobbyController(
    private val lobbyService: LobbyService,
    private val playerRepository: PlayerRepository
) {

    @PostMapping("")
    @Transactional
    fun create(
        @RequestBody @Valid body: LobbyCreateDTO,
        jwtData: JWTData
    ): ResponseEntity<LobbyCreateResponseDTO> {
        val optionalPlayer = playerRepository.findByAccountId(jwtData.accountId)
        if (!optionalPlayer.isPresent) {
            throw EntityDoesNotExistException("Player does not exist.")
        }

        val lobby = lobbyService.create(body, optionalPlayer.get())

        return ResponseEntity.ok(
            LobbyCreateResponseDTO(
                lobbyId = lobby.id
            )
        )
    }

    @PostMapping("/{lobbyId}/leave")
    @Transactional
    fun leave(
        @PathVariable lobbyId: String,
        jwtData: JWTData
    ): ResponseEntity<Void> {
        val optionalPlayer = playerRepository.findByAccountId(jwtData.accountId)
        if (!optionalPlayer.isPresent) {
            throw EntityDoesNotExistException("Player does not exist.")
        }

        val lobby = lobbyService.get(lobbyId)
        val lobbyPlayer = lobby.getPlayer(optionalPlayer.get().id)
        lobbyPlayer.leave()

        return ResponseEntity(OK)
    }

    @PostMapping("/{lobbyId}/join")
    @Transactional
    fun join(
        @PathVariable lobbyId: String,
        jwtData: JWTData
    ): ResponseEntity<List<LobbyPlayer>> {
        val optionalPlayer = playerRepository.findByAccountId(jwtData.accountId)
        if (!optionalPlayer.isPresent) {
            throw EntityDoesNotExistException("Player does not exist.")
        }

        val lobby = lobbyService.get(lobbyId)
        lobby.join(optionalPlayer.get())

        return ResponseEntity.ok(lobby.getAllPlayers())
    }

    @PostMapping("/{lobbyId}/invite")
    @Transactional
    fun invite(
        @PathVariable lobbyId: String,
        @RequestBody @Valid body: LobbyInviteDTO,
        jwtData: JWTData
    ): ResponseEntity<Void> {
        val optionalPlayer = playerRepository.findByAccountId(jwtData.accountId)
        if (!optionalPlayer.isPresent) {
            throw EntityDoesNotExistException("Player does not exist.")
        }
        val lobby = lobbyService.get(lobbyId)
        val lobbyPlayer = lobby.getPlayer(optionalPlayer.get().id)

        val invitedPlayer = playerRepository.findById(body.playerId)
        if (!invitedPlayer.isPresent) {
            throw EntityDoesNotExistException("Invited player does not exist.")
        }

        lobbyPlayer.invite(invitedPlayer.get())

        return ResponseEntity(OK)
    }

    @PostMapping("/{lobbyId}/uninvite")
    @Transactional
    fun uninvite(
        @PathVariable lobbyId: String,
        @RequestBody @Valid body: LobbyUninviteDTO,
        jwtData: JWTData
    ): ResponseEntity<Void> {
        val optionalPlayer = playerRepository.findByAccountId(jwtData.accountId)
        if (!optionalPlayer.isPresent) {
            throw EntityDoesNotExistException("Player does not exist.")
        }
        val lobby = lobbyService.get(lobbyId)
        val lobbyPlayer = lobby.getPlayer(optionalPlayer.get().id)

        lobbyPlayer.removeFromInvites(body.playerId)

        return ResponseEntity(OK)
    }

    @PostMapping("/{lobbyId}/kick")
    @Transactional
    fun kick(
        @PathVariable lobbyId: String,
        @RequestBody @Valid body: LobbyKickDTO,
        jwtData: JWTData
    ): ResponseEntity<Void> {
        val optionalPlayer = playerRepository.findByAccountId(jwtData.accountId)
        if (!optionalPlayer.isPresent) {
            throw EntityDoesNotExistException("Player does not exist.")
        }
        val lobby = lobbyService.get(lobbyId)
        val lobbyPlayer = lobby.getPlayer(optionalPlayer.get().id)

        lobbyPlayer.kick(body.playerId)

        return ResponseEntity(OK)
    }

    companion object : KLogging()
}