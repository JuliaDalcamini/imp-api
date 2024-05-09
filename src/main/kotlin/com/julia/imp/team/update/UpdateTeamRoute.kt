package com.julia.imp.team.update

import com.julia.imp.common.db.error.NotFoundItemError
import com.julia.imp.team.TeamRepository
import com.julia.imp.teammember.TeamMemberRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.patch
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.updateTeamRoute() {
    val repository by inject<TeamRepository>()
    val memberRepository by inject<TeamMemberRepository>()

    authenticate {
        patch("/teams/{id}") {
            val request = call.receive<UpdateTeamRequest>()
            val id = call.parameters["id"]
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("user.id").asString()

            val oldTeam = repository.findById(ObjectId(id))

            if (oldTeam != null) {
                val teamMember = memberRepository.findByUserIdAndTeamId(userId, id.toString())

                when {
                    teamMember == null -> call.respond(HttpStatusCode.NotFound, "Member not found")
                    !teamMember.isAdmin -> call.respond(HttpStatusCode.Unauthorized, "Only admins can delete teams")

                    else -> {
                        try {
                            repository.updateOne(
                                id = oldTeam.id,
                                team = oldTeam.copy(
                                    name = request.name
                                )
                            )

                            call.respond(HttpStatusCode.OK)
                        } catch (e: Exception) {
                            call.respond(HttpStatusCode.InternalServerError, "Failed to update team")
                        }
                    }
                }
            } else {
                throw NotFoundItemError("Team not found")
            }
        }
    }
}