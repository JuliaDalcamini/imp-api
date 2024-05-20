package com.julia.imp.team

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

fun Route.teamRoutes() {
    val service by inject<TeamService>()

    route("/teams") {
        authenticate {
            post {
                val teamId = service.create(
                    request = call.receive<CreateTeamRequest>(),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.Created, CreateTeamResponse(teamId))
            }

            patch("{id}") {
                service.update(
                    teamId = call.parameters["id"] ?: throw BadRequestException("Team ID must be provided"),
                    request = call.receive<UpdateTeamRequest>(),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.NoContent)
            }

            delete("{id}") {
                service.delete(
                    teamId = call.parameters["id"] ?: throw BadRequestException("Team ID must be provided"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}