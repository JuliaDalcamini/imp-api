package com.julia.imp.project.delete

import com.julia.imp.project.ProjectRepository
import com.julia.imp.teammember.TeamMemberRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.deleteProjectRoute() {
    val repository by inject<ProjectRepository>()
    val teamMemberRepository by inject<TeamMemberRepository>()

    authenticate {
        delete("/projects/{id}") {
            val projectId = call.parameters["id"]
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("user.id").asString()

            val project = repository.findById(ObjectId(projectId))
                ?: throw NotFoundException("Project not found")

            val user = teamMemberRepository.findByUserIdAndTeamId(userId, project.teamId)

            when {
                user == null || !user.isAdmin -> call.respond(
                    HttpStatusCode.Unauthorized,
                    "Only team admins can delete projects"
                )

                else -> {

                    try {
                        repository.deleteById(ObjectId(projectId))
                        call.respond(HttpStatusCode.OK, "Project deleted successfully")
                    } catch (error: Throwable) {
                        call.respond(HttpStatusCode.InternalServerError, "Failed to delete project")
                    }
                }
            }
        }
    }
}