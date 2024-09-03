package com.julia.imp.project

import com.julia.imp.auth.user.User
import com.julia.imp.auth.user.UserResponse
import com.julia.imp.priority.Prioritizer
import com.julia.imp.team.Team
import com.julia.imp.team.TeamResponse
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ProjectResponse(
    val id: String,
    val name: String,
    val creationDateTime: Instant,
    val creator: UserResponse,
    val prioritizer: Prioritizer,
    val totalInspectors: Int,
    val team: TeamResponse
) {

    companion object {

        fun of(project: Project, creator: User, team: Team) = ProjectResponse(
            id = project.id.toString(),
            name = project.name,
            creationDateTime = project.creationDateTime,
            creator = UserResponse.of(creator),
            prioritizer = project.prioritizer,
            totalInspectors = project.totalInspectors,
            team = TeamResponse.of(team)
        )
    }
}