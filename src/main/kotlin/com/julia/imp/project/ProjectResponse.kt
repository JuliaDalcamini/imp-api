package com.julia.imp.project

import com.julia.imp.auth.user.User
import com.julia.imp.priority.Prioritizer
import com.julia.imp.team.Team
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ProjectResponse(
    val id: String,
    val name: String,
    val creationDateTime: Instant,
    val creator: ProjectCreator,
    val prioritizer: Prioritizer,
    val team: ProjectTeam
) {

    @Serializable
    data class ProjectCreator(
        val id: String,
        val name: String
    )

    @Serializable
    data class ProjectTeam(
        val id: String,
        val name: String
    )

    companion object {

        fun of(project: Project, creator: User, team: Team) = ProjectResponse(
            id = project.id.toString(),
            name = project.name,
            creationDateTime = project.creationDateTime,
            creator = ProjectCreator(
                id = creator.id.toString(),
                name = creator.firstName
            ),
            prioritizer = project.prioritizer,
            team = ProjectTeam(
                id = team.id.toString(),
                name = team.name
            )
        )
    }
}