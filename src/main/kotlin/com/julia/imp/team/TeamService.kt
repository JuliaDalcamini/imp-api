package com.julia.imp.team

import com.julia.imp.common.db.error.ItemNotFoundException
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.team.member.Role
import com.julia.imp.team.member.TeamMember
import com.julia.imp.team.member.TeamMemberRepository
import io.ktor.server.plugins.NotFoundException
import org.bson.types.ObjectId

class TeamService(
    private val repository: TeamRepository,
    private val memberRepository: TeamMemberRepository
) {

    suspend fun create(request: CreateTeamRequest, loggedUserId: String): String {
        val teamId = repository.insert(
            Team(
                id = ObjectId(),
                name = request.name
            )
        )

        try {
            memberRepository.insert(
                TeamMember(
                    id = ObjectId(),
                    userId = loggedUserId,
                    teamId = teamId,
                    role = Role.Admin
                )
            )

            return teamId
        } catch (error: Throwable) {
            repository.deleteById(teamId)
            throw error
        }
    }

    suspend fun update(teamId: String, request: UpdateTeamRequest, loggedUserId: String) {
        val loggedMember = memberRepository.findByUserIdAndTeamId(loggedUserId, teamId)

        if (loggedMember == null || !loggedMember.isAdmin) {
            throw UnauthorizedError("Teams can only be updated by their admins")
        }

        val oldTeam = repository.findById(teamId)
            ?: throw NotFoundException("Team not found")

        repository.replaceById(
            id = oldTeam.id.toString(),
            item = oldTeam.copy(name = request.name)
        )
    }
    
    suspend fun delete(teamId: String, loggedUserId: String) {
        val loggedMember = memberRepository.findByUserIdAndTeamId(loggedUserId, teamId)

        if (loggedMember == null || !loggedMember.isAdmin) {
            throw UnauthorizedError("Only team admins can delete teams")
        }

        memberRepository.deleteByTeamId(teamId)

        try {
            repository.deleteById(teamId)
        } catch (error: ItemNotFoundException) {
            throw NotFoundException("Team not found")
        }
    }
}