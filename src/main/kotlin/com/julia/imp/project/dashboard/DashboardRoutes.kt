package com.julia.imp.project.dashboard

import com.julia.imp.auth.authenticatedUserId
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
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