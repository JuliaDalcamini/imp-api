package com.julia.imp.common.datetime

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Sum all durations returned from the [selector] lambda.
 */
inline fun <T> Iterable<T>.sumOfDuration(selector: (T) -> Duration): Duration =
    this.sumOf { selector(it).inWholeMilliseconds }.milliseconds