package com.julia.imp.team

import com.julia.imp.team.create.createTeamRoute
import io.ktor.server.routing.*

fun Route.teamRoutes() {
    createTeamRoute()
}

