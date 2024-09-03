package com.julia.imp.priority

import com.julia.imp.artifact.Artifact
import kotlinx.serialization.Serializable

@Serializable
sealed class Prioritizer {
    abstract fun calculatePriority(priority: Priority?): Double?
}
