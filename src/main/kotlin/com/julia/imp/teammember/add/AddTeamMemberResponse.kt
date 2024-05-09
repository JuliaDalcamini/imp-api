package com.julia.imp.teammember.add

import kotlinx.serialization.Serializable

@Serializable
data class AddTeamMemberResponse(
    val teamMemberId: String
)
