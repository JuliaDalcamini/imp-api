package com.julia.imp.team.update

import com.julia.imp.team.TeamRepository
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

fun Route.updateTeamRoute() {
    val repository by inject<TeamRepository>()
    val memberRepository by inject<TeamMemberRepository>()

    authenticate {
        patch("/teams/{id}") {
            val request = call.receive<UpdateTeamRequest>()
            val teamId = call.parameters["id"]
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("user.id").asString()

            val oldTeam = repository.findById(ObjectId(teamId))
                ?: throw NotFoundException("Team not found")

            val user = memberRepository.findByUserIdAndTeamId(userId, oldTeam.id.toString())

            when {
                user == null || !user.isAdmin -> call.respond(
                    HttpStatusCode.Unauthorized,
                    "Only team admins can update team name"
                )

                else -> {
                    try {
                        repository.updateOne(
                            id = oldTeam.id,
                            team = oldTeam.copy(
                                name = request.name
                            )
                        )

                        call.respond(HttpStatusCode.OK)
                    } catch (e: Throwable) {
                        call.respond(HttpStatusCode.InternalServerError, "Failed to update team")
                    }
                }
            }
        }
    }
}