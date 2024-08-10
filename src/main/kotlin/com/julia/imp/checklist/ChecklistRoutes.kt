package com.julia.imp.checklist

import com.julia.imp.artifactType.ArtifactTypeRepository
import com.julia.imp.question.QuestionRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
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

    route("checklists") {
        authenticate {
            get {
                
            }

            post {
                try {
                    repository.insert(
                        ChecklistTemplate(
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
}