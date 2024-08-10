package com.julia.imp.plugins

import com.julia.imp.artifact.artifactRoutes
import com.julia.imp.artifactType.artifactTypeRoutes
import com.julia.imp.auth.authRoutes
import com.julia.imp.checklist.checklistRoutes
import com.julia.imp.inspection.inspectionRoutes
import com.julia.imp.project.projectRoutes
import com.julia.imp.team.inspector.inspectorRoutes
import com.julia.imp.team.member.teamMemberRoutes
import com.julia.imp.team.teamRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        authRoutes()
        artifactRoutes()
        artifactTypeRoutes()
        projectRoutes()
        teamRoutes()
        teamMemberRoutes()
        inspectorRoutes()
        checklistRoutes()
        inspectionRoutes()
    }
}
