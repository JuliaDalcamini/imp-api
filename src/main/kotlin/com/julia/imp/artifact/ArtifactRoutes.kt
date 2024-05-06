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
    artifactUpdate()
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

    delete("/artifact/delete/{id}") {
        val id = call.parameters["id"]

        repository.deleteById(ObjectId(id))

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.artifactUpdate() {
    val repository by inject<ArtifactRepository>()

    authenticate {
        post("/artifact/update/{id}") {
            val request = call.receive<ArtifactRequest>()
            val id = call.parameters["id"]

            val oldArtifact = repository.findById(ObjectId(id))

            if (oldArtifact != null) {
                repository.updateOne(ObjectId(id),
                    Artifact(
                        id = oldArtifact.id,
                        name = oldArtifact.name,
                        artifactType = oldArtifact.artifactType,
                        creatorId = oldArtifact.creatorId,
                        status = true,
                        creationDateTime = oldArtifact.creationDateTime,
                        conclusionDateTime = LocalDateTime.now(),
                        priority = oldArtifact.priority
                    )
                )

                call.respond(HttpStatusCode.OK)

            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}