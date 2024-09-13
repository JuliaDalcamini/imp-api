package com.julia.imp.artifact

import kotlinx.serialization.Serializable

@Serializable
data class ArtifactReference(
    val id: String,
    val name: String
) {

    companion object {

        fun of(artifact: Artifact) = ArtifactReference(
            id = artifact.id.toString(),
            name = artifact.name
        )
    }
}