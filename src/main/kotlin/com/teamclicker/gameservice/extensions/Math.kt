package com.teamclicker.gameservice.extensions

import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

fun ClosedRange<Int>.random() =
    Random().nextInt((endInclusive + 1) - start) + start

fun Double.round(precision: Int): Double {
    val scale = 10.toDouble().pow(precision).roundToInt()
    return Math.round(this * scale).toDouble() / scale
}