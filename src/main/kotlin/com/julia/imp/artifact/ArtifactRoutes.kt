package com.julia.imp.artifact

import com.julia.imp.auth.register.RegisterRequest
import com.julia.imp.auth.user.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Route.artifactCreate() {
    val repository by inject<ArtifactRepository>()

    post("/artifact/create") {
        val request = call.receive<ArtifactRequest>()

        repository.insertOne(
            Artifact(
                id = ObjectId(),
                name = request.name,
                artifactType = TODO("nao sei oq aqui"),
                status = request.status,
                priority = request.priority,
                creatorId = TODO("nao sei oq aqui"),
                creationDateTime = LocalDateTime.now(),
                conclusionDateTime = LocalDateTime.now()
            )
        )

            call.respond(HttpStatusCode.Created)
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