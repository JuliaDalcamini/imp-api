package com.julia.imp.plugins

import com.julia.imp.artifact.artifactRoutes
import com.julia.imp.artifact.type.artifactTypeRoutes
import com.julia.imp.auth.authRoutes
import com.julia.imp.defect.defectRoutes
import com.julia.imp.inspection.inspectionRoutes
import com.julia.imp.project.dashboard.dashboardRoutes
import com.julia.imp.project.projectRoutes
import com.julia.imp.question.questionRoutes
import com.julia.imp.report.reportRoutes
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
        reportRoutes()
        dashboardRoutes()
        teamRoutes()
        teamMemberRoutes()
        inspectorRoutes()
        questionRoutes()
        inspectionRoutes()
        defectRoutes()
    }
}
