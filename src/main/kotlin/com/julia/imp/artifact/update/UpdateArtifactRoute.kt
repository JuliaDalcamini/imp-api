package com.julia.imp.artifact.update

import com.julia.imp.artifact.ArtifactRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.patch
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.updateArtifactRoute() {
    val repository by inject<ArtifactRepository>()

    authenticate {
        patch("/artifacts/{id}") {
            val request = call.receive<UpdateArtifactRequest>()
            val id = call.parameters["id"]

            val oldArtifact = repository.findById(ObjectId(id))
                ?: throw NotFoundException("Artifact not found")

            try {
                repository.updateOne(
                    id = oldArtifact.id,
                    artifact = oldArtifact.copy(
                        name = request.name,
                        artifactTypeId = request.artifactTypeId,
                        inspectorIds = request.inspectorIds,
                        priority = request.priority
                    )
                )

                call.respond(HttpStatusCode.OK)
            } catch (error: Throwable) {
                error.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}