package com.julia.imp.common.math

import kotlin.time.Duration

fun List<Double>.standardDeviation(): Double = standardDeviation(this)
fun List<Duration>.standardDeviation(): Duration = standardDeviation(this)
