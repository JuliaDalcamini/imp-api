package com.julia.imp.project.delete

import com.julia.imp.project.ProjectRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.deleteProjectRoute() {
    val repository by inject<ProjectRepository>()

    delete("/project/delete/{id}") {
        val id = call.parameters["id"]

        repository.deleteById(ObjectId(id))

        call.respond(HttpStatusCode.OK)
    }
}