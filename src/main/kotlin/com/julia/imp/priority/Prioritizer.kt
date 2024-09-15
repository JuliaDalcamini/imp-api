package com.julia.imp.priority

import kotlinx.serialization.Serializable

@Serializable
sealed class Prioritizer {
    abstract fun calculatePriority(priority: Priority?): Double?
}
