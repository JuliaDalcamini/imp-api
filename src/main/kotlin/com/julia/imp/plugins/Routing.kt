package com.julia.imp.plugins

import com.julia.imp.artifact.artifactRoutes
import com.julia.imp.auth.authRoutes
import com.julia.imp.project.projectRoutes
import com.julia.imp.team.teamRoutes
import com.julia.imp.teammember.teamMemberRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        authRoutes()
        artifactRoutes()
        projectRoutes()
        teamRoutes()
        teamMemberRoutes()
    }
}
