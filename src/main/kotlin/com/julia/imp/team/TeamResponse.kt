package com.julia.imp.team

import kotlinx.serialization.Serializable

@Serializable
data class TeamResponse(
    val id: String,
    val name: String,
    val defaultHourlyCost: Double
) {
    companion object {

        fun of(team: Team) = TeamResponse(
            id = team.id.toString(),
            name = team.name,
            defaultHourlyCost = team.defaultHourlyCost
        )
    }
}
