package com.julia.imp.teammember.add

import com.julia.imp.teammember.Role
import kotlinx.serialization.Serializable

@Serializable
data class AddTeamMemberRequest(
    val userId: String,
    val teamId: String,
    val role: Role
)