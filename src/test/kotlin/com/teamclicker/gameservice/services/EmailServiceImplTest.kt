package com.teamclicker.gameservice.services

import com.teamclicker.gameservice.kafka.KafkaTopic
import com.teamclicker.gameservice.kafka.dto.PasswordResetEmailKDTO
import com.teamclicker.gameservice.testConfig.kafka.KafkaMockConsumer
import mu.KLogging
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.concurrent.TimeUnit


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmailServiceImplTest {

    @Autowired
    lateinit var emailServiceImpl: EmailServiceImpl

    @Autowired
    lateinit var kafkaMockConsumer: KafkaMockConsumer

    @BeforeEach
    fun setUpEach() {
        kafkaMockConsumer.clearRecords()
    }

    @Nested
    inner class SendPasswordResetEmail {
        @Test
        fun `should send Kafka message`() {
            emailServiceImpl.sendPasswordResetEmail("admin@admin.com", "qwerty123")

            val received = kafkaMockConsumer.records.poll(10, TimeUnit.SECONDS)

            assertEquals(KafkaTopic.PASSWORD_RESET_EMAIL.value, received.topic())
            assertEquals(
                PasswordResetEmailKDTO("admin@admin.com", "qwerty123"),
                received.value()
            )
        }
    }

    companion object : KLogging()
}