package com.julia.imp.artifact

import com.julia.imp.artifactType.ArtifactType
import com.julia.imp.artifactType.ArtifactTypeResponse
import com.julia.imp.auth.user.User
import com.julia.imp.priority.Priority
import com.julia.imp.team.inspector.InspectorResponse
import kotlinx.serialization.Serializable

@Serializable
data class ArtifactResponse(
    val id: String,
    val name: String,
    val type: ArtifactTypeResponse,
    val projectId: String,
    val priority: Priority,
    val archived: Boolean,
    val inspectors: List<InspectorResponse>
) {

    companion object {

        fun of(artifact: Artifact, artifactType: ArtifactType, inspectors: List<User>) =
            ArtifactResponse(
                id = artifact.id.toString(),
                name = artifact.name,
                type = ArtifactTypeResponse.of(artifactType),
                projectId = artifact.projectId,
                priority = artifact.priority,
                archived = artifact.archived,
                inspectors = inspectors.map { InspectorResponse.of(it) }
            )
    }
}
