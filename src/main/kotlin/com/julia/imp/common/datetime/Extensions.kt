package com.julia.imp.common.datetime

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Sum all durations returned from the [selector] lambda.
 */
inline fun <T> Iterable<T>.sumOfDuration(selector: (T) -> Duration): Duration =
    this.sumOf { selector(it).inWholeMilliseconds }.toDuration(DurationUnit.MILLISECONDS)