package com.julia.imp.artifact

import com.julia.imp.common.db.error.ItemNotFoundException
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.project.ProjectRepository
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.member.isAdmin
import io.ktor.server.plugins.NotFoundException
import kotlinx.datetime.Clock
import org.bson.types.ObjectId

class ArtifactService(
    private val repository: ArtifactRepository,
    private val projectRepository: ProjectRepository,
    private val teamMemberRepository: TeamMemberRepository
) {

    suspend fun create(request: CreateArtifactRequest, loggedUserId: String): String {

        if(!isUserAdmin(loggedUserId, request.projectId))
            throw UnauthorizedError("Only admin can add a artifact")

        return repository.insert(
            Artifact(
                id = ObjectId(),
                name = request.name,
                artifactTypeId = request.artifactTypeId,
                projectId = request.projectId,
                creatorId = loggedUserId,
                inspectorIds = request.inspectorIds,
                creationDateTime = Clock.System.now(),
                priority = request.priority,
            )
        )
    }

    suspend fun update(artifactId: String, request: UpdateArtifactRequest, loggedUserId: String) {
        val oldArtifact = repository.findById(artifactId)
            ?: throw NotFoundException("Artifact not found")

        if (!isUserAdmin(loggedUserId, oldArtifact.projectId)) {
            throw UnauthorizedError("Only admin can update a artifact")
        }

        repository.replaceById(
            id = oldArtifact.id.toString(),
            item = oldArtifact.copy(
                name = request.name,
                artifactTypeId = request.artifactTypeId,
                inspectorIds = request.inspectorIds
            )
        )
    }

    suspend fun delete(artifactId: String, loggedUserId: String) {
        val oldArtifact = repository.findById(artifactId)
            ?: throw NotFoundException("Artifact not found")

        if(!isUserAdmin(loggedUserId, oldArtifact.projectId))
            throw UnauthorizedError("Only admin can delete a artifact")

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
}
