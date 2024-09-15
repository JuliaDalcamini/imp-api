package com.julia.imp.priority

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("moscow")
data object MoscowPrioritizer : Prioritizer() {

    override fun calculatePriority(priority: Priority?): Double? {
        if (priority !is MoscowPriority) return null

        return when (priority.level) {
            MoscowPriorityLevel.WontHave -> 0.0
            MoscowPriorityLevel.CouldHave -> 1.0
            MoscowPriorityLevel.ShouldHave -> 2.0
            MoscowPriorityLevel.MustHave -> 3.0
        }
    }
}