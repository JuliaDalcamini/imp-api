package com.julia.imp.teammember.update

import com.julia.imp.teammember.TeamMemberRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.patch
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.updateTeamMemberRoute() {
    val repository by inject<TeamMemberRepository>()

    authenticate {
        patch("/teams/members/{id}") {
            val request = call.receive<UpdateTeamMemberRequest>()
            val teamMemberId = call.parameters["id"]
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("user.id").asString()

            val oldTeamMember = repository.findById(ObjectId(teamMemberId))
                ?: throw NotFoundException("Member not found")

            val user = repository.findByUserIdAndTeamId(userId, oldTeamMember.teamId)

            when {
                user == null || !user.isAdmin -> call.respond(
                    HttpStatusCode.Unauthorized,
                    "Only team admins can update role members"
                )

                else -> {
                    try {
                        repository.updateOne(
                            id = oldTeamMember.id,
                            teamMember = oldTeamMember.copy(
                                role = request.role
                            )
                        )

                        call.respond(HttpStatusCode.OK)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, "Failed to update member")
                    }
                }
            }
        }
    }
}