package com.julia.imp.artifact

import com.julia.imp.artifact.type.ArtifactTypeRepository
import com.julia.imp.auth.user.UserRepository
import com.julia.imp.common.db.error.ItemNotFoundException
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.inspection.InspectionRepository
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
    private val inspectionRepository: InspectionRepository,
    private val teamMemberRepository: TeamMemberRepository,
    private val userRepository: UserRepository
) {

    suspend fun get(
        projectId: String,
        loggedUserId: String,
        filter: ArtifactFilter
    ): List<ArtifactResponse> {
        if (!isUserMember(loggedUserId, projectId)) {
            throw UnauthorizedError("Only team members can see artifacts")
        }

        val project = projectRepository.findById(projectId) ?: throw IllegalStateException("Project not found")
        val artifacts = repository.findFiltered(projectId, loggedUserId, filter)

        val mappedArtifacts = artifacts.map { artifact ->
            val type = typeRepository.findById(artifact.artifactTypeId)
                ?: throw IllegalStateException("Artifact type not found")

            val inspectors = artifact.inspectorIds.map {
                userRepository.findById(it) ?: throw IllegalStateException("Inspector not found")
            }

            val inspections = inspectionRepository.findByArtifactId(artifact.id.toString())
            val totalCost = inspections.sumOf { it.cost }

            val inspectedByUser = inspections.any {
                it.artifactVersion == artifact.currentVersion && it.inspectorId == loggedUserId
            }

            ArtifactResponse.of(
                artifact = artifact,
                artifactType = type,
                inspectors = inspectors,
                prioritizer = project.prioritizer,
                totalCost = totalCost,
                inspectedByUser = inspectedByUser
            )
        }

        return mappedArtifacts.sortedByDescending { it.calculatedPriority }
    }

    suspend fun get(artifactId: String, projectId: String, loggedUserId: String): ArtifactResponse {
        if (!isUserMember(loggedUserId, projectId)) {
            throw UnauthorizedError("Only team members can see artifacts")
        }

        val project = projectRepository.findById(projectId)
            ?: throw IllegalStateException("Project not found")

        val artifact = repository.findById(artifactId)
            ?: throw NotFoundException("Artifact not found")

        if (projectId != artifact.projectId) {
            throw NotFoundException("Artifact not found")
        }

        val type = typeRepository.findById(artifact.artifactTypeId)
            ?: throw IllegalStateException("Artifact type not found")

        val inspectors = artifact.inspectorIds.map {
            userRepository.findById(it) ?: throw IllegalStateException("Inspector not found")
        }

        val inspections = inspectionRepository.findByArtifactId(artifact.id.toString())
        val totalCost = inspections.sumOf { it.cost }

        val inspectedByUser = inspections.any {
            it.artifactVersion == artifact.currentVersion && it.inspectorId == loggedUserId
        }

        return ArtifactResponse.of(
            artifact = artifact,
            artifactType = type,
            inspectors = inspectors,
            prioritizer = project.prioritizer,
            totalCost = totalCost,
            inspectedByUser = inspectedByUser
        )
    }

    suspend fun create(request: CreateArtifactRequest, projectId: String, loggedUserId: String): ArtifactResponse {
        if (!isUserAdmin(loggedUserId, projectId)) {
            throw UnauthorizedError("Only admin can add artifacts")
        }

        val artifact = repository.insertAndGet(
            Artifact(
                name = request.name,
                externalLink = request.externalLink,
                artifactTypeId = request.artifactTypeId,
                projectId = projectId,
                creatorId = loggedUserId,
                inspectorIds = request.inspectorIds,
                creationDateTime = Clock.System.now(),
                priority = request.priority,
                archived = false,
                lastModification = Clock.System.now(),
                currentVersion = request.currentVersion,
                inspected = false
            )
        )

        val project = projectRepository.findById(projectId)
            ?: throw IllegalStateException("Project not found")

        val type = typeRepository.findById(artifact.artifactTypeId)
            ?: throw IllegalStateException("Artifact type not found")

        val inspectors = artifact.inspectorIds.map {
            userRepository.findById(it) ?: throw IllegalStateException("Inspector not found")
        }

        val inspections = inspectionRepository.findByArtifactId(artifact.id.toString())
        val totalCost = inspections.sumOf { it.cost }

        val inspectedByUser = inspections.any {
            it.artifactVersion == artifact.currentVersion && it.inspectorId == loggedUserId
        }

        return ArtifactResponse.of(
            artifact = artifact,
            artifactType = type,
            inspectors = inspectors,
            prioritizer = project.prioritizer,
            totalCost = totalCost,
            inspectedByUser = inspectedByUser
        )
    }

    suspend fun archive(artifactId: String, projectId: String, loggedUserId: String) {
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
            item = oldArtifact.copy(archived = true)
        )
    }

    suspend fun update(
        request: UpdateArtifactRequest,
        artifactId: String,
        projectId: String,
        loggedUserId: String
    ): ArtifactResponse {
        if (!isUserAdmin(loggedUserId, projectId)) {
            throw UnauthorizedError("Only admin can update artifacts")
        }

        val project = projectRepository.findById(projectId)
            ?: throw IllegalStateException("Project not found")

        val oldArtifact = repository.findById(artifactId)
            ?: throw NotFoundException("Artifact not found")

        if (projectId != oldArtifact.projectId) {
            throw NotFoundException("Artifact not found")
        }

        val inspected =
            if (request.currentVersion != oldArtifact.currentVersion) false
            else oldArtifact.inspected

        val updatedArtifact = oldArtifact.copy(
            name = request.name,
            externalLink = request.externalLink,
            artifactTypeId = request.artifactTypeId,
            inspectorIds = request.inspectorIds,
            priority = request.priority,
            lastModification = Clock.System.now(),
            currentVersion = request.currentVersion,
            inspected = inspected
        )

        repository.replaceById(
            id = oldArtifact.id.toString(),
            item = updatedArtifact
        )

        val type = typeRepository.findById(updatedArtifact.artifactTypeId)
            ?: throw IllegalStateException("Artifact type not found")

        val inspectors = updatedArtifact.inspectorIds.map {
            userRepository.findById(it) ?: throw IllegalStateException("Inspector not found")
        }

        val inspections = inspectionRepository.findByArtifactId(updatedArtifact.id.toString())
        val totalCost = inspections.sumOf { it.cost }

        val inspectedByUser = inspections.any {
            it.artifactVersion == updatedArtifact.currentVersion && it.inspectorId == loggedUserId
        }

        return ArtifactResponse.of(
            artifact = updatedArtifact,
            artifactType = type,
            inspectors = inspectors,
            prioritizer = project.prioritizer,
            totalCost = totalCost,
            inspectedByUser = inspectedByUser
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
