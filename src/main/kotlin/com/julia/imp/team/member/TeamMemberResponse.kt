package com.julia.imp.team.member

import com.julia.imp.auth.user.User
import kotlinx.serialization.Serializable

@Serializable
data class TeamMemberResponse(
    val userId: String,
    val teamId: String,
    val role: Role,
    val firstName: String,
    val lastName: String,
    val hourlyCost: Double
) {

    companion object {

        fun of(teamMember: TeamMember, user: User) =
            TeamMemberResponse(
                userId = teamMember.userId,
                teamId = teamMember.teamId,
                role = teamMember.role,
                firstName = user.firstName,
                lastName = user.lastName,
                hourlyCost = teamMember.hourlyCost
            )
    }
}