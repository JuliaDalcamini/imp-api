package com.julia.imp.project

import com.julia.imp.priority.Prioritizer
import kotlinx.serialization.Serializable

@Serializable
data class ProjectRequest(
    val name: String,
    val prioritizer: Prioritizer
)