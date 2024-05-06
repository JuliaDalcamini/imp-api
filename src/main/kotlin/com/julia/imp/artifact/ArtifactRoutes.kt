package com.julia.imp.artifact

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

fun Route.artifactRoutes() {
    artifactCreate()
    artifactDelete()
}

fun Route.artifactCreate() {
    val repository by inject<ArtifactRepository>()

    authenticate {
        post("/artifact/create") {
            val request = call.receive<ArtifactRequest>()
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("user.id").asString()

            val id = repository.insertOne(
                Artifact(
                    id = ObjectId(),
                    name = request.name,
                    artifactType = request.artifactType,
                    creatorId = userId,
                    status = false,
                    creationDateTime = LocalDateTime.now(),
                    conclusionDateTime = null,
                    priority = request.priority
                )
            )

            if (id != null) {
                call.respond(HttpStatusCode.Created, CreateArtifactResponse(id))
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}

fun Route.artifactDelete() {
    val repository by inject<ArtifactRepository>()

    delete("/artifact/delete") {
        val request = call.receive<ObjectId>()

        repository.deleteById(request)

        call.respond(HttpStatusCode.OK)
    }
}