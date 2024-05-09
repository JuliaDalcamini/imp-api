package com.julia.imp.project.update

import com.julia.imp.priority.Prioritizer
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProjectRequest(
    val name: String,
    val prioritizer: Prioritizer
)