package com.julia.imp.project

import com.julia.imp.auth.authenticatedUserId
import com.julia.imp.common.networking.request.query
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.projectRoutes() {
    val service by inject<ProjectService>()

    route("projects") {
        authenticate {
            get {
                val projects = service.getAll(
                    teamId = call.query["teamId"] ?: throw BadRequestException("Missing team ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(projects)
            }

            get("{id}") {
                val project = service.get(
                    projectId = call.parameters["id"] ?: throw BadRequestException("Missing project ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(project)
            }

            post {
                val projectId = service.create(
                    request = call.receive<CreateProjectRequest>(),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.Created, CreateProjectResponse(projectId))
            }

            patch("{id}") {
                val project = service.update(
                    projectId = call.parameters["id"] ?: throw BadRequestException("Missing project ID"),
                    request = call.receive<UpdateProjectRequest>(),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(project)
            }

            delete("{id}") {
                service.delete(
                    projectId = call.parameters["id"] ?: throw BadRequestException("Missing project ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}