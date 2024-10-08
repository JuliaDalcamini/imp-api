package com.julia.imp.project

import com.julia.imp.auth.user.User
import com.julia.imp.auth.user.UserResponse
import com.julia.imp.priority.Prioritizer
import com.julia.imp.team.Team
import com.julia.imp.team.TeamResponse
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ProjectResponse(
    val id: String,
    val name: String,
    val startDate: LocalDate,
    val targetDate: LocalDate,
    val creator: UserResponse,
    val prioritizer: Prioritizer,
    val minInspectors: Int,
    val team: TeamResponse,
    val finished: Boolean
) {

    companion object {

        fun of(project: Project, creator: User, team: Team) = ProjectResponse(
            id = project.id.toString(),
            name = project.name,
            startDate = project.startDate,
            targetDate = project.targetDate,
            creator = UserResponse.of(creator),
            prioritizer = project.prioritizer,
            minInspectors = project.minInspectors,
            team = TeamResponse.of(team),
            finished = project.finished
        )
    }
}