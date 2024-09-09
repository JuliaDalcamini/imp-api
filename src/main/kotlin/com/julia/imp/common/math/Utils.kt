package com.julia.imp.common.math

import kotlin.time.Duration

fun average(total: Duration, count: Int): Duration =
    if (count > 0) total / count else Duration.ZERO

fun average(total: Double, count: Int): Double =
    if (count > 0) total / count else 0.0

fun percentage(count: Int, total: Int): Double =
    if (total > 0) count.toDouble() / total else 0.0
