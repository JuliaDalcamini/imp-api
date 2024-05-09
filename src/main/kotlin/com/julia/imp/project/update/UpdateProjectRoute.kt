package com.julia.imp.project.update

import com.julia.imp.project.ProjectRepository
import com.julia.imp.teammember.TeamMemberRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.patch
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.updateProjectRoute() {
    val repository by inject<ProjectRepository>()
    val memberRepository by inject<TeamMemberRepository>()

    authenticate {
        patch("/projects/{id}") {
            val request = call.receive<UpdateProjectRequest>()
            val projectId = call.parameters["id"]
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("user.id").asString()

            val oldProject = repository.findById(ObjectId(projectId))
                ?: throw NotFoundException("Project not found")

            val user = memberRepository.findByUserIdAndTeamId(userId, oldProject.id.toString())

            when {
                user == null || !user.isAdmin -> call.respond(
                    HttpStatusCode.Unauthorized,
                    "Only team admins can update projects"
                )

                else -> {
                    try {
                        repository.updateOne(
                            id = oldProject.id,
                            project = oldProject.copy(
                                name = request.name,
                                prioritizer = request.prioritizer
                            )
                        )

                        call.respond(HttpStatusCode.OK)
                    } catch (error: Throwable) {
                        call.respond(HttpStatusCode.InternalServerError, "Failed to update project")
                    }
                }
            }
        }
    }
}