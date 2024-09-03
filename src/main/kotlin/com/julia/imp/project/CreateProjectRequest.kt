package com.julia.imp.project

import com.julia.imp.priority.Prioritizer
import kotlinx.serialization.Serializable

@Serializable
data class CreateProjectRequest(
    val name: String,
    val prioritizer: Prioritizer,
    val totalInspectors: Int,
    val teamId: String
)