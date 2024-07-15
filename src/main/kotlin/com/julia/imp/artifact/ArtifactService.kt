package com.julia.imp.artifact

import com.julia.imp.artifactType.ArtifactTypeRepository
import com.julia.imp.common.db.error.ItemNotFoundException
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.project.ProjectRepository
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.member.isAdmin
import com.julia.imp.team.member.isMember
import io.ktor.server.plugins.NotFoundException
import kotlinx.datetime.Clock

class ArtifactService(
    private val repository: ArtifactRepository,
    private val typeRepository: ArtifactTypeRepository,
    private val projectRepository: ProjectRepository,
    private val teamMemberRepository: TeamMemberRepository
) {

    suspend fun get(
        projectId: String,
        loggedUserId: String,
        filter: ArtifactFilter
    ): List<ArtifactListResponseEntry> {
        if (!isUserMember(loggedUserId, projectId)) {
            throw UnauthorizedError("Only team members can see artifacts")
        }

        val artifacts = repository.findFiltered(projectId, loggedUserId, filter)

        return artifacts.map { artifact ->
            val type = typeRepository.findById(artifact.artifactTypeId)
                ?: throw IllegalStateException("Artifact type not found")

            ArtifactListResponseEntry.of(
                artifact = artifact,
                artifactType = type
            )
        }
    }

    suspend fun create(request: CreateArtifactRequest, projectId: String, loggedUserId: String): String {
        if (!isUserAdmin(loggedUserId, projectId)) {
            throw UnauthorizedError("Only admin can add artifacts")
        }

        return repository.insert(
            Artifact(
                name = request.name,
                artifactTypeId = request.artifactTypeId,
                projectId = projectId,
                creatorId = loggedUserId,
                inspectorIds = request.inspectorIds,
                creationDateTime = Clock.System.now(),
                priority = request.priority,
                archived = false
            )
        )
    }

    suspend fun update(request: UpdateArtifactRequest, artifactId: String, projectId: String, loggedUserId: String) {
        if (!isUserAdmin(loggedUserId, projectId)) {
            throw UnauthorizedError("Only admin can update artifacts")
        }

        val oldArtifact = repository.findById(artifactId)
            ?: throw NotFoundException("Artifact not found")

        if (projectId != oldArtifact.projectId) {
            throw NotFoundException("Artifact not found")
        }

        repository.replaceById(
            id = oldArtifact.id.toString(),
            item = oldArtifact.copy(
                name = request.name,
                artifactTypeId = request.artifactTypeId,
                inspectorIds = request.inspectorIds,
                priority = request.priority,
                archived = request.archived
            )
        )
    }

    suspend fun delete(artifactId: String, projectId: String, loggedUserId: String) {
        if (!isUserAdmin(loggedUserId, projectId)) {
            throw UnauthorizedError("Only admin can delete artifacts")
        }

        val oldArtifact = repository.findById(artifactId)
            ?: throw NotFoundException("Artifact not found")

        if (projectId != oldArtifact.projectId) {
            throw NotFoundException("Artifact not found")
        }

        try {
            repository.deleteById(artifactId)
        } catch (error: ItemNotFoundException) {
            throw NotFoundException("Artifact not found")
        }
    }

    private suspend fun isUserAdmin(loggedUserId: String, projectId: String): Boolean {
        val project = projectRepository.findById(projectId) ?: return false
        return teamMemberRepository.isAdmin(loggedUserId, project.teamId)
    }

    private suspend fun isUserMember(loggedUserId: String, projectId: String): Boolean {
        val project = projectRepository.findById(projectId) ?: return false
        return teamMemberRepository.isMember(loggedUserId, project.teamId)
    }
}
