package com.teamclicker.gameservice.game.core

import com.teamclicker.gameservice.extensions.random
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class EventReceiverTest {

    @Test
    fun `should foo`() {
        val eventReceiver = EventReceiver(1, 5, 200)
        eventReceiver.start()
        launch {
            while (true) {
                for (i in 1..500) {
                    eventReceiver.queueTask {
                        Array((1..10000).random()) {
                            Object()
                            it.toLong()
                        }
                    }
                }
                delay(100)
            }
        }
        Thread.sleep(5000000)
    }

    @Nested
    inner class SaveLoadEntity {
        lateinit var eventReceiver: EventReceiver

        @BeforeEach
        fun setUp() {
            eventReceiver = EventReceiver(1, 5, 30)
            eventReceiver.recentLoadBuffer[0] = Float.MAX_VALUE
        }

        @Test
        fun `should save 0 value when executionTime was 0`() {
            eventReceiver.saveLoadEntity(0)

            assertEquals(0f, eventReceiver.recentLoadBuffer[0])
        }

        @Test
        fun `should save (0,1) value when executionTime was (0, tickRate)`() {
            eventReceiver.saveLoadEntity(20)

            assertEquals(0.1f, eventReceiver.recentLoadBuffer[0])
        }

        @Test
        fun `should save 1 value when exeucutionTime was tickRate`() {
            eventReceiver.saveLoadEntity(200)

            assertEquals(1f, eventReceiver.recentLoadBuffer[0])
        }

        @Test
        fun `should save (1, x) when executionTime was (tickRate, x)`() {
            eventReceiver.saveLoadEntity(350)

            assertEquals(1.75f, eventReceiver.recentLoadBuffer[0])
        }

        @Test
        fun `should throw error when executionTime was less than 0`() {
            assertThrows(IllegalArgumentException::class.java) {
                eventReceiver.saveLoadEntity(-2)
            }
        }
    }
}