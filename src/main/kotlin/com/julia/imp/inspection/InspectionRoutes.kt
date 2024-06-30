package com.julia.imp.inspection

import com.julia.imp.auth.authenticatedUserId
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.inspectionRoutes() {
    val service by inject<InspectionService>()

    route("/artifacts/{artifactId}/inspections") {
        authenticate {
            post {
                val inspectionId = service.create(
                    artifactId = call.parameters["artifactId"] ?: throw BadRequestException("Missing artifact ID"),
                    request = call.receive<CreateInspectionRequest>(),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.Created, CreateInspectionResponse(inspectionId))
            }

            get {
                val inspections = service.getAll(
                    artifactId = call.parameters["artifactId"] ?: throw BadRequestException("Missing artifact ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(inspections)
            }
        }
    }
}

