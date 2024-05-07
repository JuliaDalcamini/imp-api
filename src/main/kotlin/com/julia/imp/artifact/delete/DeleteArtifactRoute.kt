package com.julia.imp.artifact.delete

import com.julia.imp.artifact.ArtifactRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.deleteArtifactRoute() {
    val repository by inject<ArtifactRepository>()

    delete("/artifact/delete/{id}") {
        val id = call.parameters["id"]

        repository.deleteById(ObjectId(id))

        call.respond(HttpStatusCode.OK)
    }
}