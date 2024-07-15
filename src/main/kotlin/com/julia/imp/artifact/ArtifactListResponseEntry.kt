package com.julia.imp.artifact

import com.julia.imp.artifactType.ArtifactType
import com.julia.imp.artifactType.ArtifactTypeResponse
import com.julia.imp.priority.Priority
import kotlinx.serialization.Serializable

@Serializable
data class ArtifactListResponseEntry(
    val id: String,
    val name: String,
    val type: ArtifactTypeResponse,
    val projectId: String,
    val priority: Priority,
    val archived: Boolean
) {

    companion object {

        fun of(artifact: Artifact, artifactType: ArtifactType) =
            ArtifactListResponseEntry(
                id = artifact.id.toString(),
                name = artifact.name,
                type = ArtifactTypeResponse(
                    id = artifactType.id.toString(),
                    name = artifactType.name
                ),
                projectId = artifact.projectId,
                priority = artifact.priority,
                archived = artifact.archived
            )
    }
}
