package com.julia.imp.priority

import com.julia.imp.artifact.Artifact
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("wiegers")
data class WiegersPrioritizer(
    val userValueWeight: Double,
    val complexityWeight: Double,
    val impactWeight: Double
) : Prioritizer() {

    override fun calculatePriority(priority: Priority?): Double? {
        if (priority !is WiegersPriority) return null

        return priority.userValue * this.userValueWeight +
                priority.complexity * this.complexityWeight +
                priority.impact * this.impactWeight
    }
}
