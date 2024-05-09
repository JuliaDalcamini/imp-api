package com.julia.imp.project.create

import com.julia.imp.project.Project
import com.julia.imp.project.ProjectRepository
import com.julia.imp.teammember.TeamMemberRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.datetime.Clock
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.createProjectRoute() {
    val repository by inject<ProjectRepository>()
    val teamMemberRepository by inject<TeamMemberRepository>()

    authenticate {
        post("/projects") {
            val request = call.receive<CreateProjectRequest>()
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("user.id").asString()

            val user = teamMemberRepository.findByUserIdAndTeamId(userId, request.teamId)

            when {
                user == null || !user.isAdmin -> call.respond(
                    HttpStatusCode.Unauthorized,
                    "Only team admins can add new projects"
                )

                else -> {
                    try {
                        val projectId = repository.insertOne(
                            Project(
                                id = ObjectId(),
                                name = request.name,
                                creationDateTime = Clock.System.now(),
                                creatorId = userId,
                                prioritizer = request.prioritizer,
//                    checklist = null,
//                    artifactsList = listOf(),
                                teamId = request.teamId
                            )
                        )

                        call.respond(HttpStatusCode.Created, CreateProjectResponse(projectId))
                    } catch (error: Throwable) {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }
        }
    }
}