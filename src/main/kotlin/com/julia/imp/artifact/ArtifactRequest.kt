package com.julia.imp.artifact

import kotlinx.serialization.Serializable

@Serializable
data class ArtifactRequest(
    val name: String,
    val artifactType: String,
    val priority: String
)