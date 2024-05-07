package com.julia.imp.priority

import com.julia.imp.artifact.Artifact
import kotlinx.serialization.Serializable

@Serializable
sealed class Prioritizer {
    abstract fun sort(artifacts: List<Artifact>): List<Artifact>
}
