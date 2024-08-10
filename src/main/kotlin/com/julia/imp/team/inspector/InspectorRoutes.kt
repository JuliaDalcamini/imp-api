package com.julia.imp.team.inspector

import com.julia.imp.auth.authenticatedUserId
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.inspectorRoutes() {
    val service by inject<InspectorService>()

    route("teams/{teamId}/inspectors") {
        authenticate {
            get {
                val members = service.get(
                    teamId = call.parameters["teamId"] ?: throw BadRequestException("Missing team ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(members)
            }
        }
    }
}
