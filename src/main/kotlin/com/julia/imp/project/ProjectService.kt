package com.julia.imp.project

import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.auth.user.UserRepository
import com.julia.imp.common.db.error.ItemNotFoundException
import com.julia.imp.common.networking.error.ForbiddenError
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.inspection.InspectionRepository
import com.julia.imp.team.TeamRepository
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.member.isAdmin
import com.julia.imp.team.member.isMember
import io.ktor.server.plugins.NotFoundException
import kotlinx.datetime.Clock

class ProjectService(
    private val repository: ProjectRepository,
    private val artifactRepository: ArtifactRepository,
    private val inspectionRepository: InspectionRepository,
    private val teamMemberRepository: TeamMemberRepository,
    private val userRepository: UserRepository,
    private val teamRepository: TeamRepository
) {

    suspend fun getAll(teamId: String, loggedUserId: String, filter: ProjectFilter): List<ProjectResponse> {
        if (!teamMemberRepository.isMember(loggedUserId, teamId)) {
            throw UnauthorizedError("Only team members can see its projects")
        }

        val team = teamRepository.findById(teamId)
            ?: throw NotFoundException("Team not found")

        val projects = repository.findFiltered(teamId, filter)

        return projects.map { project ->
            val creator = userRepository.findById(project.creatorId)
                ?: throw IllegalStateException("Project creator not found")

            ProjectResponse.of(
                project = project,
                creator = creator,
                team = team
            )
        }
    }

    suspend fun get(projectId: String, loggedUserId: String): ProjectResponse {
        val project = repository.findById(projectId)
            ?: throw NotFoundException("Project not found")

        if (!teamMemberRepository.isMember(loggedUserId, project.teamId)) {
            throw UnauthorizedError("Only team members can see its projects")
        }

        val creator = userRepository.findById(project.creatorId)
            ?: throw IllegalStateException("Project creator not found")

        val team = teamRepository.findById(project.teamId)
            ?: throw IllegalStateException("Team not found")

        return ProjectResponse.of(
            project = project,
            creator = creator,
            team = team
        )
    }

    suspend fun create(request: CreateProjectRequest, loggedUserId: String): String {
        if (!teamMemberRepository.isAdmin(loggedUserId, request.teamId)) {
            throw UnauthorizedError("Only team admins can add new projects")
        }

        return repository.insert(
            Project(
                name = request.name,
                startDate = request.startDate,
                targetDate = request.targetDate,
                creatorId = loggedUserId,
                prioritizer = request.prioritizer,
                minInspectors = request.minInspectors,
                teamId = request.teamId,
                finished = false,
                finishedAt = null
            )
        )
    }

    suspend fun update(projectId: String, request: UpdateProjectRequest, loggedUserId: String): ProjectResponse {
        val oldProject = repository.findById(projectId)
            ?: throw NotFoundException("Project not found")

        if (!teamMemberRepository.isAdmin(loggedUserId, oldProject.teamId)) {
            throw UnauthorizedError("Only team admins can update projects")
        }

        if (oldProject.finished) {
            throw ForbiddenError("Can't update finished project")
        }

        val newProject = repository.replaceByIdAndGet(
            id = oldProject.id.toString(),
            item = oldProject.copy(
                name = request.name,
                minInspectors = request.minInspectors,
                startDate = request.startDate,
                targetDate = request.targetDate,
                finished = request.finished,
                finishedAt = if (request.finished) Clock.System.now() else null
            )
        )

        updateArtifactInspectionState(newProject)

        val creator = userRepository.findById(newProject.creatorId)
            ?: throw IllegalStateException("Project creator not found")

        val team = teamRepository.findById(newProject.teamId)
            ?: throw IllegalStateException("Team not found")

        return ProjectResponse.of(
            project = newProject,
            creator = creator,
            team = team
        )
    }

    suspend fun delete(projectId: String, loggedUserId: String) {
        val project = repository.findById(projectId)
            ?: throw NotFoundException("Project not found")

        if (!teamMemberRepository.isAdmin(loggedUserId, project.teamId)) {
            throw UnauthorizedError("Only team admins can delete projects")
        }

        try {
            repository.deleteById(projectId)
        } catch (error: ItemNotFoundException) {
            throw NotFoundException("Project not found")
        }
    }

    private suspend fun updateArtifactInspectionState(project: Project) {
        val artifacts = artifactRepository.findByProjectId(project.id)

        artifacts.forEach { artifact ->
            updateArtifactInspectionState(artifact, project)
        }
    }

    private suspend fun updateArtifactInspectionState(artifact: Artifact, project: Project) {
        val inspections = inspectionRepository.findByArtifactId(artifact.id)
        val inspectionCount = inspections.count { it.artifactVersion == artifact.currentVersion }
        val inspected = inspectionCount >= project.minInspectors

        if (inspected && !artifact.inspected) {
            artifactRepository.replaceById(
                id = artifact.id,
                item = artifact.copy(inspected = true)
            )
        }
    }
}