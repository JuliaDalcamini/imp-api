package com.julia.imp.team

import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.common.db.error.ItemNotFoundException
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.inspection.InspectionRepository
import com.julia.imp.inspection.answer.InspectionAnswerRepository
import com.julia.imp.project.ProjectRepository
import com.julia.imp.team.member.Role
import com.julia.imp.team.member.TeamMember
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.member.isAdmin
import io.ktor.server.plugins.NotFoundException

class TeamService(
    private val repository: TeamRepository,
    private val inspectionAnswerRepository: InspectionAnswerRepository,
    private val inspectionRepository: InspectionRepository,
    private val artifactRepository: ArtifactRepository,
    private val projectRepository: ProjectRepository,
    private val teamMemberRepository: TeamMemberRepository
) {

    suspend fun get(loggedUserId: String): List<TeamResponse> {
        val teamIds = teamMemberRepository.findTeamsByUserId(loggedUserId)
        val teams = teamIds.mapNotNull { repository.findById(it) }

        return teams.map { TeamResponse.of(it) }
    }

    suspend fun create(request: CreateTeamRequest, loggedUserId: String): TeamResponse {
        val team = repository.insertAndGet(
            Team(
                name = request.name,
                defaultHourlyCost = DEFAULT_HOURLY_COST
            )
        )

        try {
            teamMemberRepository.insert(
                TeamMember(
                    userId = loggedUserId,
                    teamId = team.id.toString(),
                    role = Role.Admin,
                    hourlyCost = team.defaultHourlyCost
                )
            )

            return TeamResponse.of(team)
        } catch (error: Throwable) {
            repository.deleteById(team.id)
            throw error
        }
    }

    suspend fun update(teamId: String, request: UpdateTeamRequest, loggedUserId: String): TeamResponse {

        if (!isUserAdmin(loggedUserId, teamId)) {
            throw UnauthorizedError("Teams can only be updated by their admins")
        }

        val oldTeam = repository.findById(teamId)
            ?: throw NotFoundException("Team not found")

        val updatedTeam = repository.replaceByIdAndGet(
            id = oldTeam.id.toString(),
            item = oldTeam.copy(
                name = request.name,
                defaultHourlyCost = request.defaultHourlyCost
            )
        )

        return TeamResponse.of(updatedTeam)
    }

    suspend fun delete(teamId: String, loggedUserId: String) {

        if (!isUserAdmin(loggedUserId, teamId)) {
            throw UnauthorizedError("Only team admins can delete teams")
        }

        val projects = projectRepository.findByTeamId(teamId)

        projects.forEach { project ->
            val artifacts = artifactRepository.findByProjectId(project.id.toString())

            artifacts.forEach { artifact ->
                val inspections = inspectionRepository.findByArtifactId(artifact.id)

                inspections.forEach { inspection ->
                    inspectionAnswerRepository.deleteAllByInspectionId(inspection.id.toString())
                }

                inspectionRepository.deleteAllByArtifactId(artifact.id.toString())
            }

            artifactRepository.deleteAllByProjectId(project.id.toString())
        }

        projectRepository.deleteAllByTeamId(teamId)
        teamMemberRepository.deleteByTeamId(teamId)

        try {
            repository.deleteById(teamId)
        } catch (error: ItemNotFoundException) {
            throw NotFoundException("Team not found")
        }
    }

    private suspend fun isUserAdmin(loggedUserId: String, teamId: String): Boolean {
        return teamMemberRepository.isAdmin(loggedUserId, teamId)
    }

    companion object {
        private const val DEFAULT_HOURLY_COST = 15.62
    }
}