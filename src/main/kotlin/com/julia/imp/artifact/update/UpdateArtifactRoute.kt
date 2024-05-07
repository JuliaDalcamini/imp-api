package com.julia.imp.artifact.update

import com.julia.imp.artifact.ArtifactRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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