package com.julia.imp.artifact

import com.julia.imp.artifact.type.ArtifactType
import com.julia.imp.artifact.type.ArtifactTypeResponse
import com.julia.imp.auth.user.User
import com.julia.imp.auth.user.UserResponse
import com.julia.imp.priority.Prioritizer
import com.julia.imp.priority.Priority
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ArtifactResponse(
    val id: String,
    val name: String,
    val externalLink: String,
    val type: ArtifactTypeResponse,
    val projectId: String,
    val priority: Priority?,
    val archived: Boolean,
    val inspectors: List<UserResponse>,
    val calculatedPriority: Double?,
    val lastModification: Instant,
    val currentVersion: String,
    val totalCost: Double,
    val fullyInspected: Boolean,
    val inspectedByUser: Boolean
) {

    companion object {

        fun of(
            artifact: Artifact,
            artifactType: ArtifactType,
            inspectors: List<User>,
            prioritizer: Prioritizer,
            totalCost: Double,
            inspectedByUser: Boolean
        ) =
            ArtifactResponse(
                id = artifact.id.toString(),
                name = artifact.name,
                externalLink = artifact.externalLink,
                type = ArtifactTypeResponse.of(artifactType),
                projectId = artifact.projectId,
                priority = artifact.priority,
                archived = artifact.archived,
                inspectors = inspectors.map { UserResponse.of(it) },
                calculatedPriority = prioritizer.calculatePriority(artifact.priority),
                lastModification = artifact.lastModification,
                currentVersion = artifact.currentVersion,
                totalCost = totalCost,
                fullyInspected = artifact.inspected,
                inspectedByUser = inspectedByUser
            )
    }
}
