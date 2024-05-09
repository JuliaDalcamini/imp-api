package com.julia.imp.teammember.remove

import com.julia.imp.teammember.TeamMemberRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.removeTeamMemberRoute() {
    val repository by inject<TeamMemberRepository>()

    authenticate {
        delete("/teams/members/{id}") {
            val teamMemberId = call.parameters["id"]
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("user.id").asString()

            val teamMember = repository.findById(ObjectId(teamMemberId))
                ?: throw NotFoundException("Member not found")

            val user = repository.findByUserIdAndTeamId(userId, teamMember.teamId)

            when {
                user == null || !user.isAdmin -> call.respond(
                    HttpStatusCode.Unauthorized,
                    "Only team admins can delete members"
                )

                else -> {
                    try {
                        repository.deleteById(ObjectId(teamMemberId))
                        call.respond(HttpStatusCode.OK, "Member deleted successfully")
                    } catch (error: Throwable) {
                        call.respond(HttpStatusCode.InternalServerError, "Failed to delete member")
                    }
                }
            }
        }
    }
}