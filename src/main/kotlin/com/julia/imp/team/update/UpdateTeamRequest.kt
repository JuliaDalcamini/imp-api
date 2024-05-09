package com.julia.imp.team.update

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTeamRequest(
    val name: String
)