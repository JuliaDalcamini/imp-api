package com.julia.imp.artifact

import com.julia.imp.priority.Priority
import kotlinx.serialization.Serializable

@Serializable
data class UpdateArtifactRequest(
    val name: String,
    val artifactTypeId: String,
    val inspectorIds: List<String>,
    val priority: Priority,
    val archived: Boolean
)