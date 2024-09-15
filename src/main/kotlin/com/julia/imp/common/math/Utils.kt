package com.julia.imp.common.math

import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun average(total: Double, count: Int): Double =
    if (count > 0) total / count else 0.0

fun average(total: Duration, count: Int): Duration =
    if (count > 0) total / count else Duration.ZERO

fun standardDeviation(values: List<Double>): Double {
    if (values.isEmpty()) return 0.0

    val mean = values.average()
    val variance = values.map { (it - mean).pow(2) }.average()

    return sqrt(variance)
}

fun standardDeviation(values: List<Duration>): Duration {
    if (values.isEmpty()) return Duration.ZERO

    val valuesInMillis = values.map { it.inWholeMilliseconds }
    val mean = valuesInMillis.average()
    val variance = valuesInMillis.map { (it - mean).pow(2) }.average()

    return sqrt(variance).milliseconds
}

fun percentage(count: Int, total: Int): Double =
    if (total > 0) count.toDouble() / total else 0.0
