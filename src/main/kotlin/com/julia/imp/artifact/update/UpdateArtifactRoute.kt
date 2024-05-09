package com.julia.imp.artifact.update

import com.julia.imp.artifact.ArtifactRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.updateArtifactRoute() {
    val repository by inject<ArtifactRepository>()

    authenticate {
        post("/artifact/update/{id}") {
            val request = call.receive<UpdateArtifactRequest>()
            val id = call.parameters["id"]

            val oldArtifact = repository.findById(ObjectId(id))

            if (oldArtifact != null) {
                repository.updateOne(
                    id = oldArtifact.id,
                    artifact = oldArtifact.copy(
                        name = request.name,
                        inspectors = request.inspectors,
                        priority = request.priority
                    )
                )

                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}