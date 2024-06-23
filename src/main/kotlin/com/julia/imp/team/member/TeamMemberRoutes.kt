package com.julia.imp.team.member

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

fun Route.teamMemberRoutes() {
    val service by inject<TeamMemberService>()

    route("/teams/{teamId}/members") {
        authenticate {
            get("{userId}") {
                service.get(
                    teamId = call.parameters["teamId"] ?: throw BadRequestException("Missing team ID"),
                    userId = call.parameters["userId"] ?: throw BadRequestException("Missing team member user ID"),
                    loggedUserId = call.authenticatedUserId
                )
            }

            post {
                val newMemberId = service.add(
                    teamId = call.parameters["teamId"] ?: throw BadRequestException("Missing team ID"),
                    request = call.receive<AddTeamMemberRequest>(),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.Created, AddTeamMemberResponse(newMemberId))
            }

            patch("{userId}") {
                service.update(
                    teamId = call.parameters["teamId"] ?: throw BadRequestException("Missing team ID"),
                    userId = call.parameters["userId"] ?: throw BadRequestException("Missing team member user ID"),
                    request = call.receive<UpdateTeamMemberRequest>(),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.NoContent)
            }

            delete("{userId}") {
                service.remove(
                    teamId = call.parameters["teamId"] ?: throw BadRequestException("Missing team ID"),
                    userId = call.parameters["userId"] ?: throw BadRequestException("Missing team member user ID"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
