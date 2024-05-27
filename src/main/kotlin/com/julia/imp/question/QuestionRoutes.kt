package com.julia.imp.question

import com.julia.imp.team.TeamService
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.questionRoutes() {
    val service by inject<TeamService>()

    route("/questions") {
        authenticate {
            get {}
        }
    }
}