package com.julia.imp.priority

import com.julia.imp.artifact.Artifact
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("moscow")
data object MoscowPrioritizer : Prioritizer() {

    override fun sort(artifacts: List<Artifact>): List<Artifact> =
        artifacts.sortedByDescending { calculatePriority(it.priority) }

    private fun calculatePriority(priority: Priority?): Int {
        if (priority !is MoscowPriority) throw IllegalArgumentException("Priority is not a MoscowPriority")

        return when (priority.level) {
            MoscowPriorityLevel.WontHave -> 0
            MoscowPriorityLevel.CouldHave -> 1
            MoscowPriorityLevel.ShouldHave -> 2
            MoscowPriorityLevel.MustHave -> 3
        }
    }
}