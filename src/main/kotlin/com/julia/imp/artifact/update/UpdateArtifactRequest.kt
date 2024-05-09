package com.julia.imp.artifact.update

import com.julia.imp.priority.Priority
import kotlinx.serialization.Serializable

@Serializable
data class UpdateArtifactRequest(
    val name: String,
    val artifactTypeId: String,
    val inspectorIdList: List<String>,
    val priority: Priority
)