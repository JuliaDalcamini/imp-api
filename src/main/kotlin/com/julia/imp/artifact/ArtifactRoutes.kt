package com.julia.imp.artifact

import com.julia.imp.auth.authenticatedUserId
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

fun Route.artifactRoutes() {
    val service by inject<ArtifactService>()

    route("/projects/{projectId}/artifacts") {
        authenticate {
            get {
                val artifacts = service.getAll(
                    projectId = call.parameters["projectId"] ?: throw BadRequestException("Missing project ID"),
                    loggedUserId = call.authenticatedUserId,
                    filter = call.parameters["filter"]?.let { ArtifactFilter.fromString(it) } ?: ArtifactFilter.Active
                )

                call.respond(artifacts)
            }

            post {
                val artifactId = service.create(
                    request = call.receive<CreateArtifactRequest>(),
                    projectId = call.parameters["projectId"] ?: throw BadRequestException("Missing project ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.Created, CreateArtifactResponse(artifactId))
            }

            patch("{id}") {
                service.update(
                    request = call.receive<UpdateArtifactRequest>(),
                    artifactId = call.parameters["id"] ?: throw BadRequestException("Missing artifact ID"),
                    projectId = call.parameters["projectId"] ?: throw BadRequestException("Missing project ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.NoContent)
            }

            delete("{id}") {
                service.delete(
                    artifactId = call.parameters["id"] ?: throw BadRequestException("Missing artifact ID"),
                    projectId = call.parameters["projectId"] ?: throw BadRequestException("Missing project ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}

