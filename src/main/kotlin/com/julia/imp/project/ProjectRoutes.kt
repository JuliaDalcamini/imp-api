package com.julia.imp.project

import com.julia.imp.project.create.createProjectRoute
import com.julia.imp.project.delete.deleteProjectRoute
import com.julia.imp.project.update.updateProjectRoute
import io.ktor.server.routing.Route

fun Route.projectRoutes() {
    createProjectRoute()
    deleteProjectRoute()
    updateProjectRoute()
}