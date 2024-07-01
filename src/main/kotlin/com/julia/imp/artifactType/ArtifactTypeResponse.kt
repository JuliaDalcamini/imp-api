package com.julia.imp.artifactType

import kotlinx.serialization.Serializable

@Serializable
data class ArtifactTypeResponse(
    val id: String,
    val name: String
)
