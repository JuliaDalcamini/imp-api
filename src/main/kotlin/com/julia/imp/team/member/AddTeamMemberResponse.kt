package com.julia.imp.team.member

import kotlinx.serialization.Serializable

@Serializable
data class AddTeamMemberResponse(
    val teamMemberId: String
)
