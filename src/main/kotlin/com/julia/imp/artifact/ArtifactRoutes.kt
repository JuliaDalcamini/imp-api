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
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.artifactRoutes() {
    val service by inject<ArtifactService>()

    route("/artifacts") {
        authenticate {
            post {
                val artifactId = service.create(
                    request = call.receive<CreateArtifactRequest>(),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.Created, CreateArtifactResponse(artifactId))
            }

            patch("{id}") {
                service.update(
                    artifactId = call.parameters["id"] ?: throw BadRequestException("Missing artifact ID"),
                    request = call.receive<UpdateArtifactRequest>(),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.NoContent)
            }

            delete("{id}") {
                service.delete(
                    artifactId = call.parameters["id"] ?: throw BadRequestException("Missing artifact ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}

