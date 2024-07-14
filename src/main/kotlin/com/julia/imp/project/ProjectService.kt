package com.julia.imp.project

import com.julia.imp.auth.user.UserRepository
import com.julia.imp.common.db.error.ItemNotFoundException
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.team.TeamRepository
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.member.isAdmin
import com.julia.imp.team.member.isMember
import io.ktor.server.plugins.NotFoundException
import kotlinx.datetime.Clock

class ProjectService(
    private val repository: ProjectRepository,
    private val teamMemberRepository: TeamMemberRepository,
    private val userRepository: UserRepository,
    private val teamRepository: TeamRepository
) {

    suspend fun getAll(teamId: String, loggedUserId: String): List<ProjectResponse> {
        if (!teamMemberRepository.isMember(loggedUserId, teamId)) {
            throw UnauthorizedError("Only team members can see its projects")
        }

        val team = teamRepository.findById(teamId)
            ?: throw NotFoundException("Team not found")

        val projects = repository.findByTeamId(teamId)

        return projects.map { project ->
            val creator = userRepository.findById(project.creatorId)
                ?: throw NotFoundException("Project creator not found")

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
            ?: throw NotFoundException("Project creator not found")

        val team = teamRepository.findById(project.teamId)
            ?: throw NotFoundException("Team not found")

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
                creationDateTime = Clock.System.now(),
                creatorId = loggedUserId,
                prioritizer = request.prioritizer,
                teamId = request.teamId
            )
        )
    }

    suspend fun update(projectId: String, request: UpdateProjectRequest, loggedUserId: String) {
        val oldProject = repository.findById(projectId)
            ?: throw NotFoundException("Project not found")

        if (!teamMemberRepository.isAdmin(loggedUserId, oldProject.teamId)) {
            throw UnauthorizedError("Only team admins can update projects")
        }

        repository.replaceById(
            id = oldProject.id.toString(),
            item = oldProject.copy(name = request.name)
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
}