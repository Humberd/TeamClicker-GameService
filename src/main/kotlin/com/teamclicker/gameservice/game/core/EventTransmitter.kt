package com.teamclicker.gameservice.game.core

import com.teamclicker.gameservice.game.workers.AbstractTaskWorker

class EventTransmitter(
    threadId: Int,
    fps: Int,
    loadBufferSize: Int
) : AbstractTaskWorker(
    "EventTransmitter-$threadId",
    fps,
    loadBufferSize
)
