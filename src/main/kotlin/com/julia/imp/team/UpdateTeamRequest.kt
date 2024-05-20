package com.julia.imp.team

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTeamRequest(
    val name: String
)