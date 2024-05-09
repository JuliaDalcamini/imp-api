package com.julia.imp.teammember.update

import com.julia.imp.teammember.Role
import kotlinx.serialization.Serializable

@Serializable
data class UpdateTeamMemberRequest(
    val role: Role
)