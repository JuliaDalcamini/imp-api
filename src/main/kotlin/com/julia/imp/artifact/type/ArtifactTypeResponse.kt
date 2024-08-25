package com.julia.imp.artifact.type

import kotlinx.serialization.Serializable

@Serializable
data class ArtifactTypeResponse(
    val id: String,
    val name: String
) {

    companion object {

        fun of(artifactType: ArtifactType) =
            ArtifactTypeResponse(
                id = artifactType.id.toString(),
                name = artifactType.name
            )
    }
}
