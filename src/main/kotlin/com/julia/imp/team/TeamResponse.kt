package com.julia.imp.team

import kotlinx.serialization.Serializable

@Serializable
data class TeamResponse(
    val id: String,
    val name: String
) {
    companion object {

        fun of(team: Team) = TeamResponse(
            id = team.id.toString(),
            name = team.name
        )
    }
}
