package com.julia.imp.defect

import com.julia.imp.auth.authenticatedUserId
import com.julia.imp.common.networking.request.query
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.defectRoutes() {
    val service by inject<DefectService>()

    route("projects/{projectId}/artifacts/{artifactId}/defects") {
        authenticate {
            get {
                val defects = service.get(
                    artifactId = call.parameters["artifactId"] ?: throw BadRequestException("Missing artifact ID"),
                    projectId = call.parameters["projectId"] ?: throw BadRequestException("Missing project ID"),
                    loggedUserId = call.authenticatedUserId,
                    filter = call.query["filter"]?.let { DefectFilter.fromString(it) } ?: DefectFilter.All
                )

                call.respond(defects)
            }

            patch("{id}") {
                val defect = service.update(
                    request = call.receive<UpdateDefectRequest>(),
                    defectId = call.parameters["id"] ?: throw BadRequestException("Missing defect ID"),
                    artifactId = call.parameters["artifactId"] ?: throw BadRequestException("Missing artifact ID"),
                    projectId = call.parameters["projectId"] ?: throw BadRequestException("Missing project ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(defect)
            }
        }
    }
}