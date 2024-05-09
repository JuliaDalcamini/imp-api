package com.julia.imp.teammember.add

import com.julia.imp.teammember.TeamMember
import com.julia.imp.teammember.TeamMemberRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.addTeamMemberRoute() {
    val memberRepository by inject<TeamMemberRepository>()

    authenticate {
        post("/teams/members") {
            val request = call.receive<AddTeamMemberRequest>()



            val memberId = memberRepository.insertOne(
                TeamMember(
                    id = ObjectId(),
                    userId = request.userId,
                    teamId = request.teamId,
                    role = request.role
                )
            )

            call.respond(HttpStatusCode.Created, AddTeamMemberResponse(memberId))
        }
    }
}