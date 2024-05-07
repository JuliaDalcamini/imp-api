package com.julia.imp.project.update

import com.julia.imp.project.ProjectRepository
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.updateProjectRoute() {
    val repository by inject<ProjectRepository>()

//    authenticate {
//        post("/project/update/{id}") {
//            val request = call.receive<ProjectRequest>()
//            val id = call.parameters["id"]
//
//            val oldProject = repository.findById(ObjectId(id))
//
//            if (oldProject != null) {
//                repository.updateOne(ObjectId(id),
//                    Project(
//
//                    )
//                )
//
//                call.respond(HttpStatusCode.OK)
//
//            } else {
//                call.respond(HttpStatusCode.NotFound)
//            }
//        }
//    }
}