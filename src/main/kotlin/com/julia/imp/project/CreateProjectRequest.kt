package com.julia.imp.project

import com.julia.imp.priority.Prioritizer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CreateProjectRequest(
    val name: String,
    val startDate: LocalDate,
    val targetDate: LocalDate,
    val prioritizer: Prioritizer,
    val minInspectors: Int,
    val teamId: String
)