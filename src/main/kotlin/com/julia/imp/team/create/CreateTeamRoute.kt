package com.julia.imp.team.create

import com.julia.imp.team.Team
import com.julia.imp.team.TeamRepository
import com.julia.imp.team.member.Role
import com.julia.imp.team.member.TeamMember
import com.julia.imp.team.member.TeamMemberRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.errors.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.createTeamRoute() {
    val repository by inject<TeamRepository>()
    val memberRepository by inject<TeamMemberRepository>()

    authenticate {
        post("/team/create") {
            val request = call.receive<CreateTeamRequest>()
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("user.id").asString()

            val teamId = repository.insertOne(
                Team(
                    id = ObjectId(),
                    name = request.name
                )
            ) ?: throw IOException("Failed to create team")

            memberRepository.insertOne(
                TeamMember(
                    id = ObjectId(),
                    userId = userId,
                    teamId = teamId,
                    role = Role.Admin
                )
            ) ?: throw IOException("Failed to create member")

            call.respond(HttpStatusCode.Created, CreateTeamResponse(teamId))
        }
    }
}