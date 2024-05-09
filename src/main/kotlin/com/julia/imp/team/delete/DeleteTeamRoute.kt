package com.julia.imp.team.delete

import com.julia.imp.team.TeamRepository
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

fun Route.deleteTeamRoute() {
    val repository by inject<TeamRepository>()
    val memberRepository by inject<TeamMemberRepository>()

    authenticate {
        delete("/teams/{id}") {
            val teamId = call.parameters["id"]
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("user.id").asString()

            val team = repository.findById(ObjectId(teamId))
                ?: throw NotFoundException("Team not found")

            val user = memberRepository.findByUserIdAndTeamId(userId, team.id.toString())

            when {
                user == null || !user.isAdmin -> call.respond(
                    HttpStatusCode.Unauthorized,
                    "Only team admins can delete teams"
                )

                else -> {
                    try {
                        repository.deleteById(ObjectId(teamId))
                        memberRepository.deleteByTeamId(ObjectId(teamId))
                        call.respond(HttpStatusCode.OK, "Team deleted successfully")
                    } catch (error: Throwable) {
                        call.respond(HttpStatusCode.InternalServerError, "Failed to delete team")
                    }
                }
            }
        }
    }
}