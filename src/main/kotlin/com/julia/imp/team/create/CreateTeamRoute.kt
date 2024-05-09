package com.julia.imp.team.create

import com.julia.imp.common.db.error.DuplicateItemError
import com.julia.imp.team.Team
import com.julia.imp.team.TeamRepository
import com.julia.imp.teammember.Role
import com.julia.imp.teammember.TeamMember
import com.julia.imp.teammember.TeamMemberRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.createTeamRoute() {
    val repository by inject<TeamRepository>()
    val memberRepository by inject<TeamMemberRepository>()

    authenticate {
        post("/teams") {
            val request = call.receive<CreateTeamRequest>()
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("user.id").asString()

            val teamId = repository.insertOne(
                Team(
                    id = ObjectId(),
                    name = request.name
                )
            )

            try {
                memberRepository.insertOne(
                    TeamMember(
                        id = ObjectId(),
                        userId = userId,
                        teamId = teamId,
                        role = Role.Admin
                    )
                )

                call.respond(HttpStatusCode.Created, CreateTeamResponse(teamId))
            } catch (error: Throwable) {
                repository.deleteById(ObjectId(teamId))

                call.respond(
                    when (error) {
                        is DuplicateItemError -> HttpStatusCode.Conflict
                        else -> HttpStatusCode.InternalServerError
                    }
                )
            }
        }
    }
}