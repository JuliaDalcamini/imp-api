package com.julia.imp.artifact

import kotlinx.serialization.Serializable

@Serializable
data class CreateArtifactResponse(
    val artifactId: String
)
