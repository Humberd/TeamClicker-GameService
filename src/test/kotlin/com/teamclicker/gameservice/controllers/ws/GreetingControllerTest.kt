package com.teamclicker.gameservice.controllers.ws

import com.teamclicker.gameservice.controllers.helpers.GreetingControllerHelper
import com.teamclicker.gameservice.controllers.helpers.Users.ALICE
import com.teamclicker.gameservice.repositories.PlayerRepository
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GreetingControllerTest {
    @LocalServerPort
    private val port: Int = 0
    @Autowired
    lateinit var playerRepository: PlayerRepository
    @Autowired
    lateinit var greetingHelper: GreetingControllerHelper

    @Nested
    inner class Greetings {
        @Test
        fun `should test`() {
            val env = System.getenv()
            greetingHelper.sub()
                .with(ALICE)
                .name("Bob")
                .subscribe {
                    println("New message: $it")
                }

            greetingHelper.send()
                .with(ALICE)
                .send(Foo("Bob"))

            val completableFuture = CompletableFuture<Foo>()

            val state = completableFuture.get(1000000, TimeUnit.SECONDS)
            assertNotNull(state)
        }
    }
}