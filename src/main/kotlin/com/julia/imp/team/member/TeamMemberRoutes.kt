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
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.teamMemberRoutes() {
    val service by inject<TeamMemberService>()

    route("/teams/{teamId}/members") {
        authenticate {
            post {
                val newMemberId = service.add(
                    teamId = call.parameters["teamId"] ?: throw BadRequestException("Team ID must be provided"),
                    request = call.receive<AddTeamMemberRequest>(),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.Created, AddTeamMemberResponse(newMemberId))
            }

            patch("{id}") {
                service.update(
                    teamId = call.parameters["teamId"] ?: throw BadRequestException("Team ID must be provided"),
                    memberId = call.parameters["id"] ?: throw BadRequestException("Team member ID must be provided"),
                    request = call.receive<UpdateTeamMemberRequest>(),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.NoContent)
            }

            delete("{id}") {
                service.remove(
                    teamId = call.parameters["teamId"] ?: throw BadRequestException("Team ID must be provided"),
                    memberId = call.parameters["id"] ?: throw BadRequestException("Team member ID must be provided"),
                    loggedUserId = call.authenticatedUserId
                )

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
