package com.julia.imp.artifact

import com.julia.imp.priority.Priority
import kotlinx.serialization.Serializable

@Serializable
data class CreateArtifactRequest(
    val name: String,
    val externalLink: String,
    val artifactTypeId: String,
    val priority: Priority?,
    val inspectorIds: List<String>,
    val currentVersion: String
)