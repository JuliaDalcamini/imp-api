package com.julia.imp.team.inspector

import com.julia.imp.auth.user.UserRepository
import com.julia.imp.common.networking.error.UnauthorizedError
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.member.isMember
import io.ktor.server.plugins.NotFoundException

class InspectorService(
    private val teamMemberRepository: TeamMemberRepository,
    private val userRepository: UserRepository
) {

    suspend fun get(teamId: String, loggedUserId: String): List<InspectorResponse> {
        if (!teamMemberRepository.isMember(loggedUserId, teamId)) {
            throw UnauthorizedError("Only team members can see team inspectors")
        }

        val teamMembers = teamMemberRepository.findInspectorsByTeamId(teamId)

        return teamMembers.map { member ->
            val user = userRepository.findById(member.userId)
                ?: throw NotFoundException("Member not found")

            InspectorResponse.of(user)
        }
    }
}