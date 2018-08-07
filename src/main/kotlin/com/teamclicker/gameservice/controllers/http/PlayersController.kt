package com.teamclicker.gameservice.controllers.http

import com.teamclicker.gameservice.defaults.PlayerDefaults
import com.teamclicker.gameservice.exceptions.EntityAlreadyExistsException
import com.teamclicker.gameservice.exceptions.EntityDoesNotExistException
import com.teamclicker.gameservice.models.dao.PlayerDAO
import com.teamclicker.gameservice.models.dto.CreatePlayerDTO
import com.teamclicker.gameservice.models.dto.UpdatePlayerDTO
import com.teamclicker.gameservice.repositories.PlayerRepository
import com.teamclicker.gameservice.security.JWTData
import com.teamclicker.gameservice.security.isAuthenticated
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.transaction.Transactional
import javax.validation.Valid

@CrossOrigin
@RestController
@RequestMapping("/api/players")
class PlayersController(
    private val playerRepository: PlayerRepository,
    private val playerDefaults: PlayerDefaults
) {

    @ApiOperation(
        value = "Creates a new player",
        notes = "Creates a new player account with predefined default values"
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Player created successfully"),
            ApiResponse(code = 400, message = "Invalid request body"),
            ApiResponse(code = 410, message = "Player already exists")
        ]
    )
    @PreAuthorize(isAuthenticated)
    @PostMapping("")
    @Transactional
    fun create(
        @RequestBody @Valid body: CreatePlayerDTO,
        jwtData: JWTData
    ): ResponseEntity<Void> {
        val playerExists = playerRepository.existsByName(body.name.toLowerCase())
        if (!playerExists) {
            throw EntityAlreadyExistsException("Player with this name already exists")
        }

        val newPlayer = playerDefaults.newPlayer(jwtData.accountId, body.name)

        playerRepository.save(newPlayer)

        return ResponseEntity(HttpStatus.OK)
    }

    @PreAuthorize(isAuthenticated)
    @GetMapping("/{playerId}")
    @Transactional
    fun read(@PathVariable playerId: Long): ResponseEntity<PlayerDAO> {
        return ResponseEntity(HttpStatus.OK)
    }

    @PreAuthorize(isAuthenticated)
    @GetMapping("")
    @Transactional
    fun readAll(): ResponseEntity<List<PlayerDAO>> {
        return ResponseEntity(HttpStatus.OK)
    }

    @ApiOperation(
        value = "Updates the player",
        notes = "Updates the player"
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Player created successfully"),
            ApiResponse(code = 400, message = "Invalid request body"),
            ApiResponse(code = 411, message = "Player does not exist")
        ]
    )
    @PreAuthorize(isAuthenticated)
    @PutMapping("/{playerId}")
    @Transactional
    fun update(
        @RequestBody @Valid body: UpdatePlayerDTO,
        @PathVariable playerId: Long,
        jwtData: JWTData
    ): ResponseEntity<Void> {
        val player = playerRepository.findBy(
            playerId = playerId,
            accountId = jwtData.accountId
        )

        if (!player.isPresent) {
            throw EntityDoesNotExistException("Player does not exist")
        }

        player.get().also {
            it.name = body.name
        }

        return ResponseEntity(HttpStatus.OK)
    }

    @ApiOperation(
        value = "Deletes the player",
        notes = "Deletes the player"
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Player deleted successfully"),
            ApiResponse(code = 411, message = "Player does not exist")
        ]
    )
    @PreAuthorize(isAuthenticated)
    @DeleteMapping("/{playerId}")
    @Transactional
    fun delete(
        @PathVariable playerId: Long,
        jwtData: JWTData
    ): ResponseEntity<Void> {
        val playerExists = playerRepository.existsBy(
            playerId = playerId,
            accountId = jwtData.accountId
        )

        if (!playerExists) {
            throw EntityDoesNotExistException("Player does not exist")
        }

        playerRepository.deleteById(playerId)

        return ResponseEntity(HttpStatus.OK)
    }

}