package com.julia.imp.artifact.delete

import com.julia.imp.artifact.ArtifactRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.deleteArtifactRoute() {
    val repository by inject<ArtifactRepository>()

    authenticate {
        delete("/artifacts/{id}") {
            val artifactId = call.parameters["id"]

            try {
                repository.deleteById(ObjectId(artifactId))
                call.respond(HttpStatusCode.OK, "Artifact deleted successfully")
            } catch (error: Throwable) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to delete artifact")
            }
        }
    }
}