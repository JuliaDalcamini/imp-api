package com.julia.imp.project.create

import com.julia.imp.project.Project
import com.julia.imp.project.ProjectRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject
import java.time.LocalDateTime

fun Route.createProjectRoute() {
    val repository by inject<ProjectRepository>()

    authenticate {
        post("/project/create") {
            val request = call.receive<CreateProjectRequest>()
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("user.id").asString()

            val id = repository.insertOne(
                Project(
                    id = ObjectId(),
                    name = request.name,
                    creationDateTime = LocalDateTime.now(),
                    creatorId = userId,
                    prioritizer = request.prioritizer,
//                    checklist = null,
//                    artifactsList = listOf(),
                    teamId = request.teamId
                )
            )

            if (id != null) {
                call.respond(HttpStatusCode.Created, CreateProjectResponse(id))
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}