package com.julia.imp.team

import kotlinx.serialization.Serializable

@Serializable
data class CreateTeamResponse(
    val teamId: String
)
