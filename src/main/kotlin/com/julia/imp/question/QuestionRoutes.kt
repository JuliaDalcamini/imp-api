package com.julia.imp.question

import com.julia.imp.common.networking.request.query
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.questionRoutes() {
    val service by inject<QuestionService>()

    route("questions") {
        authenticate {
            get {
                val questions = service.getAll(
                    artifactTypeId = call.query["artifactTypeId"]
                        ?: throw BadRequestException("Missing artifact type ID")
                )

                call.respond(questions)
            }
        }
    }
}