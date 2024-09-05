package com.julia.imp.team

import kotlinx.serialization.Serializable

@Serializable
data class CreateTeamRequest(
    val name: String,
    val defaultHourlyCost: Double
)