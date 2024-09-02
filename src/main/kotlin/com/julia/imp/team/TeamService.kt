package com.julia.imp.team

import com.julia.imp.common.db.error.ItemNotFoundException
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.team.member.Role
import com.julia.imp.team.member.TeamMember
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.member.isAdmin
import io.ktor.server.plugins.NotFoundException

class TeamService(
    private val repository: TeamRepository,
    private val teamMemberRepository: TeamMemberRepository
) {

    suspend fun get(loggedUserId: String): List<TeamResponse> {
        val teamIds = teamMemberRepository.findTeamsByUserId(loggedUserId)
        val teams = teamIds.mapNotNull { repository.findById(it) }

        return teams.map { TeamResponse.of(it) }
    }

    suspend fun create(request: CreateTeamRequest, loggedUserId: String): TeamResponse {
        val team = repository.insertAndGet(Team(name = request.name))

        try {
            teamMemberRepository.insert(
                TeamMember(
                    userId = loggedUserId,
                    teamId = team.id.toString(),
                    role = Role.Admin
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
            item = oldTeam.copy(name = request.name)
        )

        return TeamResponse.of(updatedTeam)
    }

    suspend fun delete(teamId: String, loggedUserId: String) {

        if (!isUserAdmin(loggedUserId, teamId)) {
            throw UnauthorizedError("Only team admins can delete teams")
        }

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

}