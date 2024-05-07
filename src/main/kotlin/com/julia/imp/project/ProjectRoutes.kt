package com.julia.imp.project

import com.julia.imp.project.create.CreateProjectRequest
import com.julia.imp.project.create.CreateProjectResponse
import com.julia.imp.project.create.createProjectRoute
import com.julia.imp.project.delete.deleteProjectRoute
import com.julia.imp.project.update.updateProjectRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject
import java.time.LocalDateTime

fun Route.projectRoutes() {
    createProjectRoute()
    deleteProjectRoute()
    updateProjectRoute()
}