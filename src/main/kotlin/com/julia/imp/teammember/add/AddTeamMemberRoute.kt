package com.julia.imp.teammember.add

import com.julia.imp.auth.user.UserRepository
import com.julia.imp.common.db.error.DuplicateItemError
import com.julia.imp.teammember.TeamMember
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
import io.ktor.server.routing.post
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.addTeamMemberRoute() {
    val repository by inject<TeamMemberRepository>()
    val userRepository by inject<UserRepository>()

    authenticate {
        post("/teams/members") {
            val request = call.receive<AddTeamMemberRequest>()
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("user.id").asString()

            val foundNewMember = userRepository.findById(ObjectId(request.userId))
                ?: throw NotFoundException("User not found")

            val user = repository.findByUserIdAndTeamId(userId, request.teamId)

            when {
                user == null || !user.isAdmin -> call.respond(
                    HttpStatusCode.Unauthorized,
                    "Only team admins can add new member to the team"
                )

                else -> {
                    try {

                        val newMemberId = repository.insertOne(
                            TeamMember(
                                id = ObjectId(),
                                userId = foundNewMember.id.toString(),
                                teamId = request.teamId,
                                role = request.role
                            )
                        )

                        call.respond(HttpStatusCode.Created, AddTeamMemberResponse(newMemberId))
                    } catch (error: DuplicateItemError) {
                        call.respond(HttpStatusCode.Conflict)
                    } catch (error: Throwable) {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }
        }
    }
}