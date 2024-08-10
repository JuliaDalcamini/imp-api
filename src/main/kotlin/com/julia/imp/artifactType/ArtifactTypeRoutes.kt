package com.julia.imp.artifactType

import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.artifactTypeRoutes() {
    val service by inject<ArtifactTypeService>()

    route("artifact-types") {
        authenticate {
            get { call.respond(service.get()) }
        }
    }
}

