package com.julia.imp.artifact.create

import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.datetime.Clock
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.createArtifactRoute() {
    val repository by inject<ArtifactRepository>()

    authenticate {
        post("/artifacts") {
            val request = call.receive<CreateArtifactRequest>()
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("user.id").asString()

            try {
                val artifactId = repository.insertOne(
                    Artifact(
                        id = ObjectId(),
                        name = request.name,
                        artifactTypeId = request.artifactTypeId,
                        projectId = request.projectId,
                        creatorId = userId,
                        inspectorIds = request.inspectorIds,
                        creationDateTime = Clock.System.now(),
                        priority = request.priority,
                    )
                )

                call.respond(HttpStatusCode.Created, CreateArtifactResponse(artifactId))
            } catch (error: Throwable) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}