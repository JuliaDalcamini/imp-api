package com.julia.imp.report

import com.julia.imp.auth.authenticatedUserId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.reportRoutes() {
    val service by inject<ReportService>()

    route("projects/{id}/report") {
        authenticate {
            get {
                val report = service.get(
                    projectId = call.parameters["id"] ?: throw BadRequestException("Missing project ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(report)
            }
        }
    }
}

