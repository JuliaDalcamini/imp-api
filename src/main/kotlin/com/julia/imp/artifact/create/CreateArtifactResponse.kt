package com.julia.imp.artifact.create

import kotlinx.serialization.Serializable

@Serializable
data class CreateArtifactResponse(
    val artifactId: String
)
