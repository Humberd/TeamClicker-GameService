package com.teamclicker.gameservice.game.core

import com.teamclicker.gameservice.game.workers.AbstractTaskWorker

class EventReceiver(
    threadId: Int,
    fps: Int,
    loadBufferSize: Int
) : AbstractTaskWorker(
    "EventReceiver-$threadId",
    fps,
    loadBufferSize
)
