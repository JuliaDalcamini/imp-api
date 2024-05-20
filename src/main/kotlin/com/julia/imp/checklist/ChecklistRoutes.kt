package com.julia.imp.checklist

import com.julia.imp.artifactType.ArtifactTypeRepository
import com.julia.imp.question.QuestionRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

// TODO: Remover caso os templates sejam fixos no BD (sem manipulação pelo usuário)
fun Route.checklistRoutes() {
    createChecklistRoutes()
}

// TODO: Caso sejam manipulados, migrar lógica para service
fun Route.createChecklistRoutes() {
    val repository by inject<ChecklistRepository>()
    val questionRepository by inject<QuestionRepository>()
    val artifactTypeRepository by inject<ArtifactTypeRepository>()

    authenticate {
        post("/checklists") {

            try {
                repository.insert(
                    ChecklistTemplate(
                        id = ObjectId(),
                        questions = questionRepository.findAll().map { it.id.toString() },
                        artifactTypes = artifactTypeRepository.findAll().map { it.id.toString() }
                    )
                )

                call.respond(HttpStatusCode.OK)
            } catch (error: Throwable) {
                call.respond(HttpStatusCode.InternalServerError, error)
            }
        }
    }
}