package com.julia.imp.common.datetime

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus

/**
 * Sum all durations returned from the [selector] lambda.
 */
inline fun <T> Iterable<T>.sumOfDuration(selector: (T) -> Duration): Duration =
    this.sumOf { selector(it).inWholeMilliseconds }.milliseconds

/**
 * Sum all durations in the iterable.
 */
fun Iterable<Duration>.sumOfDuration(): Duration = this.sumOfDuration { it }

/**
 * Returns an [Instant] corresponding to the start of the day, at the default timezone.
 */
fun LocalDate.atStartOfDay(): Instant =
    this.atStartOfDayIn(TimeZone.currentSystemDefault())

/**
 * Returns an [Instant] corresponding to the end of the day, at the default timezone.
 */
fun LocalDate.atEndOfDay(): Instant =
    this.plus(1, DateTimeUnit.DAY)
        .atStartOfDay()
        .minus(1, DateTimeUnit.NANOSECOND)