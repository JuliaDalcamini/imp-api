package com.julia.imp.artifact.create

import com.julia.imp.priority.Priority
import kotlinx.serialization.Serializable

@Serializable
data class CreateArtifactRequest(
    val name: String,
    val artifactTypeId: String,
    val projectId: String,
    val inspectorIdList: List<String>,
    val priority: Priority
)