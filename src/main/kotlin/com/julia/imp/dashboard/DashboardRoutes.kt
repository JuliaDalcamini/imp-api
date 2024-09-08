package com.julia.imp.dashboard

import com.julia.imp.auth.authenticatedUserId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.dashboardRoutes() {
    val service by inject<DashboardService>()

    route("projects/{id}/dashboard") {
        authenticate {
            get {
                val dashboard = service.get(
                    projectId = call.parameters["id"] ?: throw BadRequestException("Missing project ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(dashboard)
            }
        }
    }
}