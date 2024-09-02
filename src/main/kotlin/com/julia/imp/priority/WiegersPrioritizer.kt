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

    override fun sort(artifacts: List<Artifact>): List<Artifact> =
        artifacts.sortedByDescending { calculatePriority(it.priority) }

    private fun calculatePriority(priority: Priority?): Double {
        if (priority !is WiegersPriority) throw IllegalArgumentException("Priority is not a WiegersPriority")

        return priority.userValue * this.userValueWeight +
                priority.complexity * this.complexityWeight +
                priority.impact * this.impactWeight
    }
}
