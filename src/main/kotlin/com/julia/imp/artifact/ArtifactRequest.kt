package com.julia.imp.artifact

import com.julia.imp.priority.Priority
import kotlinx.serialization.Serializable

@Serializable
data class ArtifactRequest(
    val name: String,
    val artifactType: String,
    val priority: Priority
)