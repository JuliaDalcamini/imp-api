package com.julia.imp.project

import com.julia.imp.artifact.Artifact
import com.julia.imp.priority.*
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

fun Route.projectRoutes() {
    projectCreate()
    projectDelete()
    projectUpdate()
}

fun Route.projectCreate() {
    val repository by inject<ProjectRepository>()

    authenticate {
        post("/project/create") {
            val request = call.receive<ProjectRequest>()
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("user.id").asString()

            val id = repository.insertOne(
                Project(
                    id = ObjectId(),
                    name = request.name,
                    creationDateTime = LocalDateTime.now(),
                    creatorId = userId,
                    prioritizer = request.prioritizer,
//                    checklist = null,
//                    artifactsList = listOf(),
                    team = listOf()
                )
            )

            // TODO: Remove
            testPrioritizer(request.prioritizer)

            if (id != null) {
                call.respond(HttpStatusCode.Created, CreateProjectResponse(id))
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}

fun Route.projectDelete() {
    val repository by inject<ProjectRepository>()

    delete("/project/delete/{id}") {
        val id = call.parameters["id"]

        repository.deleteById(ObjectId(id))

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.projectUpdate() {
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

// TODO: Remove
fun testPrioritizer(prioritizer: Prioritizer) {
    val artifacts = when (prioritizer) {
        is MoscowPrioritizer -> listOf(
            Artifact(
                id = ObjectId(),
                name = "art 1",
                artifactType = "doc",
                creatorId = "id do usuariokk",
                status = false,
                creationDateTime = LocalDateTime.now(),
                conclusionDateTime = null,
                priority = MoscowPriority(MoscowPriorityLevel.WontHave)
            ),
            Artifact(
                id = ObjectId(),
                name = "art 2",
                artifactType = "doc",
                creatorId = "id do usuariokk",
                status = false,
                creationDateTime = LocalDateTime.now(),
                conclusionDateTime = null,
                priority = MoscowPriority(MoscowPriorityLevel.MustHave)
            ),
            Artifact(
                id = ObjectId(),
                name = "art 3",
                artifactType = "doc",
                creatorId = "id do usuariokk",
                status = false,
                creationDateTime = LocalDateTime.now(),
                conclusionDateTime = null,
                priority = MoscowPriority(MoscowPriorityLevel.ShouldHave)
            ),
            Artifact(
                id = ObjectId(),
                name = "art 4",
                artifactType = "doc",
                creatorId = "id do usuariokk",
                status = false,
                creationDateTime = LocalDateTime.now(),
                conclusionDateTime = null,
                priority = MoscowPriority(MoscowPriorityLevel.WontHave)
            ),
            Artifact(
                id = ObjectId(),
                name = "art 5",
                artifactType = "doc",
                creatorId = "id do usuariokk",
                status = false,
                creationDateTime = LocalDateTime.now(),
                conclusionDateTime = null,
                priority = MoscowPriority(MoscowPriorityLevel.CouldHave)
            )
        )

        is WiegersPrioritizer -> listOf(
            Artifact(
                id = ObjectId(),
                name = "art 1",
                artifactType = "doc",
                creatorId = "id do usuariokk",
                status = false,
                creationDateTime = LocalDateTime.now(),
                conclusionDateTime = null,
                priority = WiegersPriority(3, 2, 1)
            ),
            Artifact(
                id = ObjectId(),
                name = "art 2",
                artifactType = "doc",
                creatorId = "id do usuariokk",
                status = false,
                creationDateTime = LocalDateTime.now(),
                conclusionDateTime = null,
                priority = WiegersPriority(5, 5, 1)
            ),
            Artifact(
                id = ObjectId(),
                name = "art 3",
                artifactType = "doc",
                creatorId = "id do usuariokk",
                status = false,
                creationDateTime = LocalDateTime.now(),
                conclusionDateTime = null,
                priority = WiegersPriority(1, 5, 1)
            ),
            Artifact(
                id = ObjectId(),
                name = "art 4",
                artifactType = "doc",
                creatorId = "id do usuariokk",
                status = false,
                creationDateTime = LocalDateTime.now(),
                conclusionDateTime = null,
                priority = WiegersPriority(5, 1, 5)
            ),
            Artifact(
                id = ObjectId(),
                name = "art 5",
                artifactType = "doc",
                creatorId = "id do usuariokk",
                status = false,
                creationDateTime = LocalDateTime.now(),
                conclusionDateTime = null,
                priority = WiegersPriority(5, 1, 1)
            )
        )
    }

    val sortedArtifacts = prioritizer.sort(artifacts)
    println(sortedArtifacts)
}