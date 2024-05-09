package com.julia.imp.project.create

import com.julia.imp.project.Project
import com.julia.imp.project.ProjectRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.utils.io.errors.IOException
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
            ) ?: throw IOException("Failed to create project")

            call.respond(HttpStatusCode.Created, CreateProjectResponse(id))
        }
    }
}