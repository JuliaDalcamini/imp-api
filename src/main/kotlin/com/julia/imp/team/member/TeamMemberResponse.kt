package com.julia.imp.team.member

import kotlinx.serialization.Serializable

@Serializable
data class TeamMemberResponse(
    val userId: String,
    val teamId: String,
    val role: Role
) {

    companion object {

        fun of(teamMember: TeamMember) =
            TeamMemberResponse(
                userId = teamMember.userId,
                teamId = teamMember.teamId,
                role = teamMember.role
            )
    }
}